package com.fourun.watchdogs.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fourun.watchdogs.R
import com.fourun.watchdogs.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.notAUserButton.setOnClickListener {
            // Navigate to SignUpActivity
            val intent = Intent(requireContext(), SignUpFragment::class.java)
            startActivity(intent)
        }

        binding.signInButton.setOnClickListener {
            signIn()
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.homeFragment)
    }

    private fun signIn() {
        val email = binding.emailEt.text.toString()
        val password = binding.passwordEt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Empty Fields Are not Allowed !! ", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Invalid Email Format", Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navigateToHomeFragment()
                }
            }
            .addOnFailureListener { exception ->
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(requireContext(), "Invalid Password", Toast.LENGTH_SHORT).show()
                    }
                    is FirebaseAuthInvalidUserException -> {
                        Toast.makeText(requireContext(), "User Not Found", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Sign In failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}