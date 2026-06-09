(function () {
    "use strict";

    function setError(field, message) {
        var row = field.closest(".form-row") || field.parentElement;
        var error = row ? row.querySelector(".error-message") : null;
        field.classList.toggle("is-invalid", Boolean(message));
        if (error) {
            error.textContent = message || "";
        }
    }

    function fieldValue(form, name) {
        var field = form.elements[name];
        return field ? String(field.value || "").trim() : "";
    }

    function isEmail(value) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
    }

    function isPhone(value) {
        return value === "" || /^[0-9]{10}$/.test(value);
    }

    function validateText(form, name, label, minLength) {
        var field = form.elements[name];
        if (!field) {
            return true;
        }
        var value = fieldValue(form, name);
        if (value.length < minLength) {
            setError(field, label + " must be at least " + minLength + " characters.");
            return false;
        }
        setError(field, "");
        return true;
    }

    function validateEmail(form) {
        var field = form.elements.email;
        if (!field) {
            return true;
        }
        var value = fieldValue(form, "email");
        if (!isEmail(value)) {
            setError(field, "Enter a valid email address.");
            return false;
        }
        setError(field, "");
        return true;
    }

    function validatePassword(form) {
        var field = form.elements.password;
        if (!field) {
            return true;
        }
        var value = fieldValue(form, "password");
        if (value.length < 6) {
            setError(field, "Password must be at least 6 characters.");
            return false;
        }
        setError(field, "");
        return true;
    }

    function validatePhone(form) {
        var field = form.elements.phone;
        if (!field) {
            return true;
        }
        var value = fieldValue(form, "phone");
        if (!isPhone(value)) {
            setError(field, "Phone number must contain exactly 10 digits.");
            return false;
        }
        setError(field, "");
        return true;
    }

    function validateExperience(form) {
        var field = form.elements.experience;
        if (!field) {
            return true;
        }
        var value = Number(field.value);
        if (!Number.isInteger(value) || value < 0 || value > 50) {
            setError(field, "Experience must be between 0 and 50 years.");
            return false;
        }
        setError(field, "");
        return true;
    }

    function validateChecked(form, name, label) {
        var fields = form.querySelectorAll("[name='" + name + "']");
        if (!fields.length) {
            return true;
        }
        var checked = Array.prototype.some.call(fields, function (field) {
            return field.checked;
        });
        var first = fields[0];
        if (!checked) {
            setError(first, "Select at least one " + label + ".");
            return false;
        }
        setError(first, "");
        return true;
    }

    function validateOptionalFutureDate(form, name, message) {
        var field = form.elements[name];
        if (!field || !field.value) {
            return true;
        }
        var selected = new Date(field.value);
        if (selected < new Date()) {
            setError(field, message);
            return false;
        }
        setError(field, "");
        return true;
    }

    function validateContact(form) {
        var ok = true;
        ok = validateText(form, "name", "Name", 2) && ok;
        ok = validateEmail(form) && ok;
        ok = validateText(form, "message", "Message", 10) && ok;
        return ok;
    }

    function validateForm(form) {
        var ok = true;
        var formType = form.dataset.validate;

        ok = validateEmail(form) && ok;
        ok = validatePassword(form) && ok;

        if (formType === "doctor-register") {
            ok = validateText(form, "name", "Name", 2) && ok;
            ok = validatePhone(form) && ok;
            ok = validateExperience(form) && ok;
            ok = validateChecked(form, "gender", "gender") && ok;
            ok = validateChecked(form, "days", "available day") && ok;
            ok = validateText(form, "address", "Address", 5) && ok;
        }

        if (formType === "patient-register") {
            ok = validateText(form, "name", "Name", 2) && ok;
            ok = validatePhone(form) && ok;
            ok = validateChecked(form, "gender", "gender") && ok;
            ok = validateOptionalFutureDate(form, "appointment", "Appointment must be a future date and time.") && ok;
        }

        if (formType === "contact") {
            ok = validateContact(form) && ok;
        }

        return ok;
    }

    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll("form[data-validate]").forEach(function (form) {
            form.addEventListener("submit", function (event) {
                if (!validateForm(form)) {
                    event.preventDefault();
                }
            });

            form.addEventListener("input", function (event) {
                if (event.target.matches("input, select, textarea")) {
                    validateForm(form);
                }
            });

            form.addEventListener("change", function () {
                validateForm(form);
            });
        });

        var simpleInterestButton = document.getElementById("calculate");
        if (simpleInterestButton) {
            simpleInterestButton.addEventListener("click", function () {
                var principal = Number(document.getElementById("principal").value);
                var rate = Number(document.getElementById("rate").value);
                var time = Number(document.getElementById("time").value);
                var result = document.getElementById("simpleInterestResult");

                if (principal <= 0 || rate <= 0 || time <= 0) {
                    result.textContent = "Enter positive values for all fields.";
                    return;
                }

                result.textContent = "Simple Interest: " + ((principal * rate * time) / 100).toFixed(2);
            });
        }

        var squareCubeButton = document.getElementById("calculateSquareCube");
        if (squareCubeButton) {
            squareCubeButton.addEventListener("click", function () {
                var number = Number(document.getElementById("number").value);
                var result = document.getElementById("squareCubeResult");

                if (!Number.isFinite(number)) {
                    result.textContent = "Enter a valid number.";
                    return;
                }

                result.textContent = "Square: " + (number * number) + " | Cube: " + (number * number * number);
            });
        }
    });
}());
