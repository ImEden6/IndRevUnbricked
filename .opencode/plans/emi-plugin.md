# EMI Plugin Implementation Plan

## Goal
Add conditional EMI (recipe viewer mod) integration so all Industrial Revolution items display proper recipe/usage pages in EMI. Currently only items with vanilla crafting/smelting recipes show up; items exclusively processed in custom machines (pulverizer, infuser, compressor, etc.) have no EMI visibility.

## Files to Modify

### 1. `build.gradle`
**Add TerraformersMC maven + EMI compile-only dependency:**
```groovy
// In repositories block (after draylar maven):
maven {
    name = "TerraformersMC"
    url = "https://maven.terraformersmc.com/"
    content {
        includeGroup "dev.emi"
    }
}

// In dependencies block:
modCompileOnly "dev.emi:emi-fabric:1.0.12+1.20.1:api"
```
- `modCompileOnly` = compile-time only, not bundled, not required at runtime
- Using official EMI maven (TerraformersMC) instead of cursemaven — more reliable
- The `:api` classifier only includes API classes, not the full mod (smaller footprint)

### 2. `fabric.mod.json`
Add `emi` entrypoint (auto-conditional — if EMI is absent, entrypoint is ignored):
```json
"emi": [
  { "adapter": "kotlin", "value": "me.mervyn.indrev.compat.emi.EMIPlugin" }
]
```

### 3. EmiIngredient helpers — Top-level utility functions
**`src/main/kotlin/me/mervyn/indrev/compat/emi/EmiIngredientUtil.kt`** (optional, can inline)

Helpers to convert `IRRecipe` I/O to EMI types:
- `itemInput(ingredient: Ingredient, count: Int): EmiIngredient` — maps matching stacks → `EmiStack` with count → `EmiIngredient.of(list)`
- `itemOutput(stack: ItemStack, chance: Double): EmiStack` — `EmiStack.of(stack).setChance(chance.toFloat())`
- `fluidInput(amount: IRFluidAmount): EmiIngredient` — `EmiIngredient.of(EmiStack.of(amount.resource.fluid, amount.amount()))`

## Files to Create

### 4. `src/main/kotlin/me/mervyn/indrev/compat/emi/IRMachineEmiRecipe.kt`

Implements `EmiRecipe` wrapping a single `IRRecipe`:

```kotlin
class IRMachineEmiRecipe(
    private val recipe: IRRecipe,
    private val category: EmiRecipeCategory
) : EmiRecipe {

    private val inputs: List<EmiIngredient> by lazy { buildInputs() }
    private val outputs: List<EmiStack> by lazy { buildOutputs() }

    override fun getCategory() = category
    override fun getId() = recipe.id
    override fun getInputs() = inputs
    override fun getOutputs() = outputs
    override fun getDisplayWidth() = 134
    override fun getDisplayHeight() = 66

    override fun addWidgets(widgets: WidgetHolder) {
        val startX = (widgets.width - 134) / 2
        val startY = (widgets.height - 66) / 2

        // Fluid input tank (top-left area)
        if (recipe is IRFluidRecipe && recipe.fluidInput.isNotEmpty()) {
            val fluidIngredient = EmiIngredient.of(EmiStack.of(
                recipe.fluidInput[0].resource.fluid,
                recipe.fluidInput[0].amount()))
            widgets.addTank(fluidIngredient, startX - 2, startY, 16, 32, 16000)
        }

        // Item input slots (1-2 slots on left)
        val itemInputs = inputs.filter { it !is EmiStack || it.getKey() !is Fluid }
        if (itemInputs.isNotEmpty()) {
            widgets.addSlot(itemInputs[0], startX + 1, startY + 19)
            if (itemInputs.size > 1)
                widgets.addSlot(itemInputs[1], startX - 17, startY + 19)
        }

        // Arrow
        widgets.addFillingArrow(startX + 24, startY + 18, recipe.ticks * 50)

        // Output slot
        val itemOutputs = outputs.filter { it.getKey() !is Fluid }
        if (itemOutputs.isNotEmpty()) {
            widgets.addSlot(itemOutputs[0], startX + 61, startY + 19)
                .recipeContext(this)
                .drawBack(false)
        }

        // Fluid output tank (top-right area)
        if (recipe is IRFluidRecipe && recipe.fluidOutput.isNotEmpty()) {
            val fluidIngredient = EmiIngredient.of(EmiStack.of(
                recipe.fluidOutput[0].resource.fluid,
                recipe.fluidOutput[0].amount()))
            widgets.addTank(fluidIngredient, startX + 83, startY, 16, 32, 16000)
        }
    }
}
```

**Key details:**
- `getInputs()` returns a combined list of fluids + items (fluids first, items after)
- `getOutputs()` returns a combined list of items + fluids
- The `addWidgets` method handles visual layout: 1-2 inputs on left, arrow in middle, output on right, fluid tanks above
- Output slot calls `.recipeContext(this)` (required for recipe tree / favoriting)
- Arrow animation takes `recipe.ticks * 50`ms (matching REI's process time visualization)
- The `supportsRecipeTree()` default (`!getInputs().isEmpty() && !getOutputs().isEmpty()`) works fine here

### 5. `src/main/kotlin/me/mervyn/indrev/compat/emi/EMIPlugin.kt`

Main plugin implementing `EmiPlugin`:

```kotlin
object EMIPlugin : EmiPlugin {

    private val categories = mutableMapOf<Identifier, EmiRecipeCategory>()

    override fun register(registry: EmiRegistry) {
        registerCategories(registry)
        registerRecipes(registry)
        registerWorkstations(registry)
        registerUpgradeInfo(registry)
        hideItems(registry)
    }

    private fun registerCategories(registry: EmiRegistry) {
        // For each machine type, create an EmiRecipeCategory with the machine block as icon
        // Same 9 categories as REI: pulverizer, infuser, compressor, recycler,
        // fluid infuser, condenser, smelter, sawmill, modular workbench
    }

    private fun registerRecipes(registry: EmiRegistry) {
        // Iterate registry.getRecipeManager().recipes keys
        // Find IRRecipeType instances from this mod
        // For each, look up category and register IRMachineEmiRecipe
    }

    private fun registerWorkstations(registry: EmiRegistry) {
        // For each machine type, register all tier blocks as workstations
    }

    private fun registerUpgradeInfo(registry: EmiRegistry) {
        // Register EmiInfoRecipe for tier upgrades (same text as REI)
    }

    private fun hideItems(registry: EmiRegistry) {
        registry.removeEmiStacksIf { stack ->
            stack.id.namespace == "indrev" && hide(stack.id)
        }
    }
}
```

## Key Design Decisions

| Aspect | Decision | Rationale |
|---|---|---|
| **Conditional loading** | Fabric `"emi"` entrypoint | If EMI is absent, the entrypoint class is never loaded. No mixins needed. |
| **Runtime requirement** | None (`modCompileOnly`) | EMI only needed at compile time; not in `depends` |
| **Recipe discovery** | `registry.getRecipeManager().recipes` iteration | Same approach as REI plugin; filters for `IRRecipeType` by namespace |
| **ID uniqueness** | Uses `recipe.id` from JSON | Recipe IDs are already unique (file path based) |
| **Item inputs** | `EmiIngredient.of(Ingredient, count)` | Handles tag-based ingredients correctly, preserves stack count |
| **Output chances** | `EmiStack.setChance(float)` | EMI natively renders chance < 1 with a special slot texture |
| **Fluids** | `EmiStack.of(Fluid, amount)` via `TankWidget` | Matches REI fluid display approach |
| **Arrow animation** | `FillingArrowWidget` with `recipe.ticks * 50`ms | Mirrors process time indication |
| **Upgrade info** | `EmiInfoRecipe` | Same translatable text as REI's `DefaultInformationDisplay` |
| **Hidden items** | `registry.removeEmiStacksIf` | Uses existing `hide()` from `hiddenitems.kt` |

## What This Unlocks

After implementation, all 9 machine recipe types will be visible in EMI:

| Recipe Type | Items now visible in EMI |
|---|---|
| Pulverizer | `nikolite_dust` (how to obtain), ore → dust chain |
| Infuser | `nikolite_ingot`, `enriched_nikolite_dust`, `enriched_nikolite_ingot` (how to obtain + usage) |
| Compressor | Plate/rod recipes |
| Recycler | Recycling outputs |
| Fluid Infuser | Fluid-based recipes |
| Condenser | Condensation recipes |
| Smelter | Alloy smelting |
| Sawmill | Multi-output wood processing |
| Modular Workbench | Module recipes |

Any item that is an **input** of a machine recipe will show "used in" pages for that recipe.
Any item that is an **output** of a machine recipe will show "how to craft" pages.

## Potential Concerns

1. **EMI API stability**: The 1.20 branch of EMI uses the API shown above. The curse file `580555:8081374` may differ slightly (that's `1.0.12+1.20.1`). The official maven dependency `dev.emi:emi-fabric:1.0.12+1.20.1:api` should match exactly.

2. **`RecipeManager` access**: `EmiRegistry.getRecipeManager()` returns the vanilla `RecipeManager`. We access recipes via `getAllOfType(type)` which returns `Map<Identifier, Recipe<C>>` — this is the standard 1.20.1 Fabric API.

3. **Fluid amount units**: EMI uses droplets for fluid amounts. The mod's `IRFluidAmount.amount()` returns droplets (1 bucket = 81000 droplets in Fabric Transfer API). This matches what `EmiStack.of(Fluid, long)` expects.

4. **Module workbench layout**: The modular workbench has 1-6 inputs arranged around a center output. For simplicity, the generic layout (stacked inputs) is acceptable initially; can be refined later with a dedicated recipe class if needed.
