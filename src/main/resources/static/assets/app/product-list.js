function refreshProductCount() {
    const countBadge = document.querySelector("#product-count-badge");
    if (!countBadge) {
        return;
    }
    countBadge.textContent = document.querySelectorAll("[data-product-row]").length.toString();
}

document.body.addEventListener("htmx:after:swap", () => {
    document.querySelectorAll("wa-dialog[open]").forEach((dialog) => {
        dialog.removeAttribute("open");
    });
    refreshProductCount();
});
