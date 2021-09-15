package com.inlacou.inkpasswordscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.inlacou.inkpasswordscreen.databinding.ActivityInkPasswordBinding
import com.inlacou.pripple.RippleButton

/**
 * Created by Inlacou on 13/09/2021
 */
class InkPasswordPasswordActivity : Activity(), BaseInkPasswordView {

	private var binder: ActivityInkPasswordBinding? = null
	override val etInput: AppCompatEditText? get() = binder?.include?.input
	override val tvAttempts: TextView? get() = binder?.include?.attempts
	override val tvHint: TextView? get() = binder?.include?.hint
	override val btnCheck: RippleButton? get() = binder?.include?.btnCheck

	override lateinit var md5: String
	override var hint: String? = null
	override var maxAttempts: Int? = null
	override var attempts: Int = 0

	companion object {
		private var sListener: ((status: InkPasswordAttemptStatus) -> Unit)? = null

		/**
		 * @param md5 to check the input to
		 * @param hint nullable string to help remember the password if applicable
		 * @param maxAttempts as integer nullable, unlimited attempts if null or <= 0
		 * @param listener to pass the result to
		 */
		@JvmOverloads
		fun launch(
			activity: Activity,
			md5: String,
			hint: String? = null,
			maxAttempts: Int? = null,
			listener: (status: InkPasswordAttemptStatus) -> Unit
		) {
			val intent = Intent(activity, InkPasswordPasswordActivity::class.java)
			intent.putExtra("md5", md5)
			intent.putExtra("maxAttempts", maxAttempts)
			intent.putExtra("hint", hint)
			activity.startActivity(intent)
			activity.overridePendingTransition(0, 0)
			sListener = listener
		}
	}

	public override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binder = ActivityInkPasswordBinding.inflate(layoutInflater)
		setContentView(binder?.root)

		md5 = intent.getStringExtra("md5")!!
		maxAttempts = intent.getIntExtra("maxAttempts", Int.MIN_VALUE)

		populate()
	}

	override fun onBackPressed() {
		end(InkPasswordAttemptStatus.CANCELLED)
	}

	override fun end(result: InkPasswordAttemptStatus) {
		sListener?.invoke(result)
		sListener = null
		finish()
		overridePendingTransition(0, 0)
	}
}