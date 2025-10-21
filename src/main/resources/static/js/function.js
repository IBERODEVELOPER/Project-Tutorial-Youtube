
function confirmarEdicion(event, element) {
	event.preventDefault(); // Evita la redirección inmediata
	Swal.fire({
		title: '¿Estás seguro?',
		text: "Serás redirigido para editar el elemento.",
		icon: 'info',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Sí, continuar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			// Redirige a la URL del enlace
			window.location.href = element.getAttribute('href');
		}
	});
}

function confirmarDelete(event, element) {
	event.preventDefault(); // Evita la redirección inmediata
	Swal.fire({
		title: '¿Estás seguro?',
		text: "¡No podrás revertir esto!",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#3085d6',
		cancelButtonColor: '#d33',
		confirmButtonText: 'Aceptar',
		cancelButtonText: 'Cancelar'
	}).then((result) => {
		if (result.isConfirmed) {
			Swal.fire({
				title: 'Eliminado!',
				text: 'Registro eliminado.',
				icon: 'success',
				timer: 2000, // Tiempo antes de redirigir
				showConfirmButton: false
			}).then(() => {
				// Redirige al enlace de eliminación después de mostrar el mensaje de éxito
				window.location.href = element.getAttribute('href');
			});
		}
	});
}

function togglePasswordVisibility(passwordFieldId, toggleIcon) {
	const passwordField = document.getElementById(passwordFieldId);
	const isPasswordVisible = passwordField.type === 'text';

	passwordField.type = isPasswordVisible ? 'password' : 'text';
	toggleIcon.classList.toggle('fa-eye-slash', isPasswordVisible);
	toggleIcon.classList.toggle('fa-eye', !isPasswordVisible);
}

function validateForm() {
	const newPassword = document.getElementById('newPassword').value;
	const repeatPassword = document.getElementById('repeatPassword').value;

	const newPasswordError = document.getElementById('newPasswordError');
	const repeatPasswordError = document.getElementById('repeatPasswordError');

	// Limpiar mensajes de error
	newPasswordError.textContent = '';
	repeatPasswordError.textContent = '';

	// Reglas de complejidad de la contraseña (puedes modificar estas reglas según tus necesidades)
	const minLength = 8;
	const hasUpperCase = /[A-Z]/.test(newPassword);
	const hasLowerCase = /[a-z]/.test(newPassword);
	const hasNumber = /[0-9]/.test(newPassword);
	const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(newPassword);

	let valid = true;

	if (newPassword.length < minLength || !hasUpperCase || !hasLowerCase || !hasNumber || !hasSpecialChar) {
		newPasswordError.textContent = 'La contraseña debe tener al menos 8 caracteres, incluyendo una letra mayúscula, una letra minúscula, un número y un carácter especial !@#$%^&*(),.?":{}|<>.';
		valid = false;
	}

	if (newPassword !== repeatPassword) {
		repeatPasswordError.textContent = 'Las contraseñas no coinciden.';
		valid = false;
	}

	return valid;
}
