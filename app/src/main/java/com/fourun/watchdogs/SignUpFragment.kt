package com.fourun.watchdogs.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fourun.watchdogs.R
import com.fourun.watchdogs.databinding.FragmentSignUpBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment(), View.OnFocusChangeListener {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.alreadyAUserButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.signUpButton.setOnClickListener {
            signUp()
        }

        binding.fullNameEt.onFocusChangeListener = this
        binding.emailEt.onFocusChangeListener = this
        binding.passwordEt.onFocusChangeListener = this
        binding.cPasswordEt.onFocusChangeListener = this
    }

    private fun signUp() {
        if (!validateFullName() or !validateEmail() or !validatePassword() or !validateConfirmPassword() or !validatePasswordAndConfirmPassword()) {
            return
        }

        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.loginFragment)
                } else {
                    Toast.makeText(requireContext(), "Sign Up failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateFullName(): Boolean {
        val value: String = binding.fullNameEt.text.toString()
        return if (value.isEmpty()) {
            showError(binding.fullNameTil, getString(R.string.full_name_error))
            false
        } else {
            removeError(binding.fullNameTil)
            true
        }
    }

    private fun validateEmail(): Boolean {
        val value: String = binding.emailEt.text.toString()
        return if (value.isEmpty()) {
            showError(binding.emailTil, getString(R.string.email_error))
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            showError(binding.emailTil, getString(R.string.invalid_email_error))
            false
        } else {
            removeError(binding.emailTil)
            true
        }
    }

    private fun validatePassword(): Boolean {
        val value: String = binding.passwordEt.text.toString()
        return if (value.isEmpty()) {
            showError(binding.passwordTil, getString(R.string.password_error))
            false
        } else if (value.length < 6) {
            showError(binding.passwordTil, getString(R.string.invalid_password_error))
            false
        } else {
            removeError(binding.passwordTil)
            true
        }
    }

    private fun validateConfirmPassword(): Boolean {
        val value: String = binding.cPasswordEt.text.toString()
        return if (value.isEmpty()) {
            showError(binding.cPasswordTil, getString(R.string.confirm_password_error))
            false
        } else if (value.length < 6) {
            showError(binding.cPasswordTil, getString(R.string.invalid_password_error))
            false
        } else {
            removeError(binding.cPasswordTil)
            true
        }
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        val password: String = binding.passwordEt.text.toString()
        val cPassword: String = binding.cPasswordEt.text.toString()
        return if (password != cPassword) {
            showError(binding.cPasswordTil, getString(R.string.password_not_match_error))
            false
        } else {
            removeError(binding.cPasswordTil)
            true
        }
    }

    private fun showError(textInputLayout: TextInputLayout, errorMessage: String) {
        textInputLayout.apply {
            isErrorEnabled = true
            error = errorMessage
        }
    }

    private fun removeError(textInputLayout: TextInputLayout) {
        if (textInputLayout.isErrorEnabled) {
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = null
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v?.id) {
            R.id.fullNameEt -> {
                if (!hasFocus) {
                    validateFullName()
                }
            }

            R.id.emailEt -> {
                if (!hasFocus) {
                    validateEmail()
                }
            }

            R.id.passwordEt -> {
                if (!hasFocus) {
                    validatePassword()
                }
            }

            R.id.cPasswordEt -> {
                if (!hasFocus) {
                    validateConfirmPassword()
                    validatePasswordAndConfirmPassword()
                    if (validatePassword() && validateConfirmPassword() && validatePasswordAndConfirmPassword()) {
                        binding.cPasswordTil.setStartIconDrawable(R.drawable.check_circle_24)
                        binding.cPasswordTil.setStartIconTintList(
                            ContextCompat.getColorStateList(
                                requireContext(),
                                R.color.green
                            )
                        )
                    } else {
                        binding.cPasswordTil.setStartIconDrawable(null)
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}