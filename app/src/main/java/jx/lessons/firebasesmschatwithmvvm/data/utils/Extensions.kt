package jx.lessons.firebasesmschatwithmvvm.data.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import jx.lessons.firebasesmschatwithmvvm.R


fun View.gone(){
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
}


fun View.disable(){
    isEnabled = false
}

fun View.enabled(){
    isEnabled = true
}

fun Fragment.toast(msg: String?){
    Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
}
fun Activity.toast(msg: String?){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}

fun ChipGroup.addChip_createPost(
    text: String,
    list : MutableList<String>
) {
    val chip: Chip = LayoutInflater.from(context).inflate(R.layout.item_chip_create_post,null,false) as Chip
    chip.text = if (text.length > 9) text.substring(0,9) + "..." else text
    chip.setOnCloseIconClickListener {
        removeView(chip)
        list.remove(chip.text)
    }
    addView(chip)
}
fun ChipGroup.addChip_home_post(
    text: String
) {
    val chip: Chip = LayoutInflater.from(context).inflate(R.layout.item_home_chip,null,false) as Chip
    chip.text = if (text.length > 9) text.substring(0,9) + "..." else text
    addView(chip)
}

fun Context.createDialog(layout: Int, cancelable: Boolean): Dialog {
    val dialog = Dialog(this, android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(layout)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(cancelable)
    dialog.create()
    dialog.show()
    return dialog
}

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
fun snackbar(message:String,view:View){
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}
fun Fragment.getEmail(context: Context):String{
    val sharedPref = SharedPref(context)
    return sharedPref.getEmail().toString()
}
fun firebasePathgmail(email: String):String{
    var email2 =""
    email.forEachIndexed { index, letter ->
        if (letter.isLetter() || letter.isDigit()) {
            email2+=email[index]
        }
    }
    return email2
}