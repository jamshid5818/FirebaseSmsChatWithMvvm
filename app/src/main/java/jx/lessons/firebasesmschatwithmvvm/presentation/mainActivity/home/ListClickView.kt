package jx.lessons.firebaseSmsChatWithMvvm.presentation.mainActivity.home

interface ListClickView {
    fun clickedLike(key:String)
    fun clickedComment(unixTime:Long)
    fun doubleClickImageView(key:String)
    fun longClickedLike(key:String)
    fun downloadClicked(url: String,key:String)
}