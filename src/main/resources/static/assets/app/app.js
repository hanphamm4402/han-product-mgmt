function findSubmitButton(form) {
    let button = form.querySelector('wa-button[type="submit"]');
    if (!button && form.id) {
        button = document.querySelector(`wa-button[form="${form.id}"][type="submit"]`);
    }
    return button;
}

function setButtonLoading(element, loading) {
    if (!element) {
        return;
    }

    if (element.tagName === "WA-BUTTON") {
        element.loading = loading;
        return;
    }

    if (element.tagName === "FORM") {
        const button = findSubmitButton(element);
        if (button) {
            button.loading = loading;
        }
        return;
    }

    const button = element.querySelector?.('wa-button[type="submit"]');
    if (button) {
        button.loading = loading;
    }
}

function htmxSourceElement(event) {
    return event.detail?.ctx?.sourceElement || event.detail?.elt || event.target;
}

document.addEventListener("htmx:before:request", (event) => {
    setButtonLoading(htmxSourceElement(event), true);
});

document.addEventListener("htmx:after:request", (event) => {
    setButtonLoading(htmxSourceElement(event), false);
});

document.addEventListener("htmx:beforeRequest", (event) => {
    setButtonLoading(htmxSourceElement(event), true);
});

document.addEventListener("htmx:afterRequest", (event) => {
    setButtonLoading(htmxSourceElement(event), false);
});

document.addEventListener("submit", (event) => {
    if (!event.defaultPrevented) {
        setButtonLoading(event.target, true);
    }
});
