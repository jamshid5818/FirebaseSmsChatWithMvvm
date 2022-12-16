package jx.lessons.firebasesmschatwithmvvm.data.model

class Sms {
    var smsText:String=""
    var emailAddress:String=""
    var gender:String=""
    var unixTime:Long=0
    constructor()

    constructor(smsText: String, emailAddress: String,gender:String,unixTime:Long) {
        this.smsText = smsText
        this.emailAddress = emailAddress
        this.gender=gender
        this.unixTime=unixTime
    }
}