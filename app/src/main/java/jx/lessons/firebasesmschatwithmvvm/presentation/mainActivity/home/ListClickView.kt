package jx.lessons.firebasesmschatwithmvvm.presentation.mainActivity.home

interface ListClickView {
    fun clickedLike(randomKey:String)
    fun clickedComment(randomKey:String)
    fun doubleClickImageView(randomKey:String)
    fun longClickedLike(randomKey: String)
}