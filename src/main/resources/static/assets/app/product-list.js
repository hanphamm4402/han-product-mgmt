document.body.addEventListener("htmx:afterSwap", () => {
    document.querySelectorAll("wa-dialog[open]").forEach((dialog) => {
        dialog.removeAttribute("open");
    });
});
