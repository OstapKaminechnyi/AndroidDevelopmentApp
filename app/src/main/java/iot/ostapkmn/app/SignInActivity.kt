package iot.ostapkmn.app

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.util.regex.Pattern


class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()

        btn_sign_up.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        btn_sign_in.setOnClickListener {
            if (isValidEmail(sign_in_email.text.toString())
                    && isValidPassword(sign_in_password.text.toString())) {
                signIn()
            } else {
                Toast.makeText(baseContext, "Try again",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    private fun signIn() {
        auth.signInWithEmailAndPassword(sign_in_email.text.toString(), sign_in_password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        updateUI(null)
                    }
                }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            Toast.makeText(
                    baseContext, "Login failed.",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return if (sign_in_email.text.toString().isEmpty()) {
            sign_in_email.error = "Please enter valid email"
            false
        } else pattern.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordPattern: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{8,16}$")
        return if (sign_in_password.text.toString().isEmpty()) {
            sign_in_password.error = "Please enter valid password"
            false
        } else passwordPattern.matcher(password).matches()
    }
}


