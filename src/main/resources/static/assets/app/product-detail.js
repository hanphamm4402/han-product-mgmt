const variantsList = document.querySelector("#variants-list");
const addVariantButton = document.querySelector("#add-variant-button");
const variantTemplate = document.querySelector("#variant-template");
const variantError = document.querySelector("#variant-error");
const productForm = document.querySelector("#product-form");
const availableVariantCount = document.querySelector("#available-variant-count");

function variantRows() {
    return Array.from(variantsList.querySelectorAll(".product-variant-row"));
}

function setName(element, index, field) {
    if (!element) {
        return;
    }
    element.setAttribute("name", `variants[${index}].${field}`);
}

function renumberVariants() {
    variantRows().forEach((row, index) => {
        setName(row.querySelector('[data-name="id"], input[name$=".id"]'), index, "id");
        setName(row.querySelector('[data-name="title"], wa-input[name$=".title"]'), index, "title");
        setName(row.querySelector('[data-name="sku"], wa-input[name$=".sku"]'), index, "sku");
        setName(row.querySelector('[data-name="availableHidden"], input[type="hidden"][name$=".available"]'), index, "available");
        setName(row.querySelector('[data-name="available"], wa-checkbox[name$=".available"]'), index, "available");
        setName(row.querySelector('[data-name="price"], wa-input[name$=".price"]'), index, "price");
    });
    refreshVariantState();
}

function refreshVariantState() {
    const rows = variantRows();
    const available = rows.filter((row) => row.querySelector('wa-checkbox[name$=".available"]')?.checked).length;
    availableVariantCount.textContent = available.toString();
    if (rows.length > 0) {
        variantError.classList.add("product-callout-hidden");
    }
}

function addVariant() {
    const fragment = variantTemplate.content.cloneNode(true);
    variantsList.appendChild(fragment);
    renumberVariants();
}

variantsList.addEventListener("click", (event) => {
    const button = event.target.closest(".delete-variant-button");
    if (!button) {
        return;
    }
    button.closest(".product-variant-row").remove();
    renumberVariants();
});

variantsList.addEventListener("wa-change", refreshVariantState);
variantsList.addEventListener("change", refreshVariantState);
addVariantButton.addEventListener("click", addVariant);

productForm.addEventListener("submit", (event) => {
    renumberVariants();
    if (variantRows().length === 0) {
        event.preventDefault();
        variantError.classList.remove("product-callout-hidden");
    }
});

renumberVariants();
