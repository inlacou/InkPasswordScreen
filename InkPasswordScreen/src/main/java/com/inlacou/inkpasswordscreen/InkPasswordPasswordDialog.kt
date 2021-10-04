package com.inlacou.inkpasswordscreen

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.inlacou.inkpasswordscreen.databinding.DialogInkPasswordBinding
import com.inlacou.pripple.RippleButton

class InkPasswordPasswordDialog @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
	: FrameLayout(context, attrs, defStyleAttr), BaseInkPasswordView {

	private var binder: DialogInkPasswordBinding? = null
	override val etInput: AppCompatEditText? get() = binder?.include?.input
	override val tvAttempts: TextView? get() = binder?.include?.attempts
	override val tvHint: TextView? get() = binder?.include?.hint
	override val btnCheck: RippleButton? get() = binder?.include?.btnCheck
	private val background: View? get() = binder?.background

	override lateinit var md5: String
	private var log: Boolean = false
	override var hint: String? = null
	override var maxAttempts: Int? = null
	override var attempts: Int = 0

	private lateinit var onDeleteAnimationFinished: (dialog: InkPasswordPasswordDialog, status: InkPasswordAttemptStatus) -> Unit
	private lateinit var onCancelled: (InkPasswordPasswordDialog) -> Unit
	private var cancelOnOutsideTouch: Boolean = true

	constructor(context: Context,
		md5: String,
		hint: String? = null,
		maxAttempts: Int? = null,
		log: Boolean = false,
		cancelOnOutsideTouch: Boolean,
		onCancelled: (InkPasswordPasswordDialog) -> Unit,
		onDeleteAnimationFinished: (dialog: InkPasswordPasswordDialog, status: InkPasswordAttemptStatus) -> Unit,
	): this(context) {
		this.md5 = md5
		this.hint = hint
		this.log = log
		this.maxAttempts = maxAttempts
		this.cancelOnOutsideTouch = cancelOnOutsideTouch
		this.onCancelled = onCancelled
		this.onDeleteAnimationFinished = onDeleteAnimationFinished
	}

	init {
		this.initialize()
		populate()
		background?.setOnClickListener { if(cancelOnOutsideTouch) outAnimation(onEnd = { onDeleteAnimationFinished.invoke(this, InkPasswordAttemptStatus.CANCELLED) }) }
	}

	private fun initialize() {
		if(binder==null) binder = DialogInkPasswordBinding.inflate(LayoutInflater.from(context), this, true)
	}

	fun showAsDialog(act: Activity) {
		(act.window.decorView as ViewGroup).addView(this)
		inAnimation()
	}
	fun removeAsDialog(act: Activity) = (act.window.decorView as ViewGroup).removeView(this)

	private fun inAnimation(onStart: (() -> Unit)? = null, onEnd: (() -> Unit)? = null) {
		AnimationUtils.loadAnimation(context, android.R.anim.fade_in).let { animation ->
			startAnimation(animation)
			animation.setAnimationListener(object : Animation.AnimationListener {
				override fun onAnimationStart(animation: Animation) { onStart?.invoke() }
				override fun onAnimationEnd(animation: Animation) { onEnd?.invoke() }
				override fun onAnimationRepeat(animation: Animation) {}
			})
		}
	}
	private fun outAnimation(onStart: (() -> Unit)? = null, onEnd: (() -> Unit)? = null) {
		AnimationUtils.loadAnimation(context, android.R.anim.fade_out).let { animation ->
			startAnimation(animation)
			animation.setAnimationListener(object : Animation.AnimationListener {
				override fun onAnimationStart(animation: Animation) { onStart?.invoke() }
				override fun onAnimationEnd(animation: Animation) { onEnd?.invoke() }
				override fun onAnimationRepeat(animation: Animation) {}
			})
		}
	}

	override fun end(result: InkPasswordAttemptStatus) {
		outAnimation(onEnd = {
			if(log) Log.d("InkPasswordScreen", "onEnd")
			onDeleteAnimationFinished.invoke(this, result) })
	}

	companion object {
		fun launch(
			act: Activity,
			md5: String,
			hint: String? = null,
			maxAttempts: Int? = null,
			log: Boolean = false,
			listener: (status: InkPasswordAttemptStatus) -> Unit
		) {
			InkPasswordPasswordDialog(act,
				cancelOnOutsideTouch = true,
				md5 = md5,
				hint = hint,
				log = log,
				maxAttempts = maxAttempts,
				onCancelled = { it.outAnimation(onEnd = { it.removeAsDialog(act) }) },
				onDeleteAnimationFinished = { dialog: InkPasswordPasswordDialog, status: InkPasswordAttemptStatus ->
					if(log) Log.d("InkPasswordDialog", "removeView")
					(act.window.decorView as ViewGroup).removeView(dialog)
					if(log) Log.d("InkPasswordDialog", "invoke")
					listener.invoke(status)
				}).showAsDialog(act)
		}
	}
}