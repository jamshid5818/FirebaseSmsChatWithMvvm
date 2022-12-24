package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home

import java.net.URL

interface ListClickView {
    fun clickedLike(key:String)
    fun clickedComment(unixTime:Long)
    fun doubleClickImageView(key:String)
    fun longClickedLike(key:String)
    fun downloadClicked(url: String,key:String)
}