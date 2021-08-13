package com.example.travelencer_android_2021.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GMailSender : Authenticator() {
    // 보내는 사람 이메일과 비밀번호
    val fromEmail = "Travelencer.noreply@gmail.com"
    val password = "hanium2021"

    var code = "-1"

    init {
        // 이메일 인증 코드 생성
        code = createEmailCode()
    }

    // 보내는 사람 계정 확인
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(fromEmail, password)
    }

    // 메일 보내기
    fun sendEmail(toEmail: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val props = Properties()
            props.setProperty("mail.transport.protocol", "smtp")
            props.setProperty("mail.host", "smtp.gmail.com")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.socketFactory.fallback", "false")
            props.setProperty("mail.smtp.quitwait", "false")

            // 구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달
            val session = Session.getDefaultInstance(props, this@GMailSender)

            // 메시지 객체 만들기
            val message = MimeMessage(session)
            message.sender = InternetAddress(fromEmail)                                 // 보내는 사람 설정
            message.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))    // 받는 사람 설정
            message.subject = "Travelencer 이메일 인증키 입니다."                         // 이메일 제목
            message.setText("$code \n\n 위 코드를 <인증 코드>에 입력해주세요")               // 이메일 내용

            // 전송
            Transport.send(message)
        }
    }

    // 이메일 인증 코드 생성
    private fun createEmailCode() : String {
        val str = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"
            , "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
        var code = ""
        for (i in 0..8) code += str[(Math.random() * str.size).toInt()]
        return code
    }
}