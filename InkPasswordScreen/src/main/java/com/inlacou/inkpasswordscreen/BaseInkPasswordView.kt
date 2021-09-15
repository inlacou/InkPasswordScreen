package com.inlacou.inkpasswordscreen

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.inlacou.pripple.RippleButton
import java.security.MessageDigest

interface BaseInkPasswordView {

	val etInput: AppCompatEditText?
	val tvAttempts: TextView?
	val tvHint: TextView?
	val btnCheck: RippleButton?

	var md5: String
	var hint: String?
	var maxAttempts: Int?
	var attempts: Int

	fun populate() {
		if((maxAttempts?:0) <= 0) {
			maxAttempts = null
		}

		tvHint?.text = hint
		tvHint?.visibility = View.INVISIBLE
		btnCheck?.setOnClickListener { check(etInput?.text?.toString() ?: "") }
	}

	private fun check(current: String) {
		val newMd5 = current.toMD5()
		if(md5==newMd5) end(InkPasswordAttemptStatus.GRANTED)
		else {
			attempts++
			maxAttempts.let {
				if(it!=null) {
					if(it<attempts) {
						end(InkPasswordAttemptStatus.DENIED_TOO_MANY_ATTEMPTS)
						tvHint?.visibility = View.VISIBLE
					}
				} else end(InkPasswordAttemptStatus.DENIED)
			}
			tvAttempts?.text = "$attempts/$maxAttempts"
		}
	}

	private fun String.toMD5(): String = MessageDigest.getInstance("MD5").digest(this.toByteArray()).toHex()
	private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }

	fun end(result: InkPasswordAttemptStatus)
}