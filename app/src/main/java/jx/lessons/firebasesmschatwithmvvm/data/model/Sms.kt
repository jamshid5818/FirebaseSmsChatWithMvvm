package jx.lessons.firebaseSmsChatWithMvvm.data.model

class Sms {
    var smsText:String=""
    var emailAddress:String=""
    var gender:String=""
    var unixTime:Long=0
    var imageUri:String=""
    constructor()

    constructor(smsText: String, emailAddress: String,gender:String,unixTime:Long,imageUri: String) {
        this.smsText = smsText
        this.emailAddress = emailAddress
        this.gender=gender
        this.unixTime=unixTime
        this.imageUri=imageUri
    }
}