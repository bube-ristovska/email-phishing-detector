package com.example.phishing_detector.data
import androidx.compose.ui.graphics.Color

data class SampleEmail(
    val sender: String,
    val senderInitial: String,
    val avatarColor: Color,
    val subject: String,
    val preview: String,
    val time: String,
    val isRead: Boolean,
    val body: String
)

val sampleEmails: List<SampleEmail> = listOf(
    SampleEmail(
        sender = "PayPal Security",
        senderInitial = "P",
        avatarColor = Color(0xFF003087),
        subject = "Urgent: Verify your account now",
        preview = "Your PayPal account has been limited. Click here to restore access immediately...",
        time = "9:41 AM",
        isRead = false,
        body = """Dear Customer,

We have detected unusual activity on your PayPal account. Your account has been temporarily limited.

To restore full access, please verify your identity by clicking the link below:

http://paypal-secure-verify.suspicious-domain.com/confirm

Failure to verify within 24 hours will result in permanent account suspension.

PayPal Security Team"""
    ),
    SampleEmail(
        sender = "IT Department",
        senderInitial = "I",
        avatarColor = Color(0xFF1A73E8),
        subject = "ACTION REQUIRED: Password expires today",
        preview = "Your network password will expire in 2 hours. Update it now to avoid losing access...",
        time = "8:15 AM",
        isRead = false,
        body = """Hello Employee,

Your corporate password expires TODAY at 5:00 PM.

Click here to update your password immediately:
http://company-portal.fake-login.net/reset

If you don't update your password, you will be locked out of all systems.

IT Support Help Desk"""
    ),
    SampleEmail(
        sender = "Netflix",
        senderInitial = "N",
        avatarColor = Color(0xFFE50914),
        subject = "Your payment failed - update billing info",
        preview = "We were unable to process your payment. To keep your membership, please update your...",
        time = "Yesterday",
        isRead = true,
        body = """Dear Netflix Member,

We're having some trouble with your current billing information. We'll try again, but in the meantime you may want to update your payment details.

Update payment at: http://netflix-billing-update.phishing-site.xyz/payment

Your account will be cancelled if we cannot process your payment within 3 days.

The Netflix Team"""
    ),
    SampleEmail(
        sender = "Amazon Delivery",
        senderInitial = "A",
        avatarColor = Color(0xFFFF9900),
        subject = "Your package could not be delivered",
        preview = "We attempted to deliver your package but no one was available. Reschedule delivery...",
        time = "Yesterday",
        isRead = true,
        body = """Hello,

We attempted to deliver your Amazon package today but were unable to complete the delivery.

To reschedule, please confirm your address and pay a small redelivery fee of ${'$'}2.99:

http://amazon-redelivery.fake-parcel.com/reschedule

Your package will be returned to sender if not claimed within 48 hours.

Amazon Logistics"""
    ),
    SampleEmail(
        sender = "Bank of America",
        senderInitial = "B",
        avatarColor = Color(0xFFE31837),
        subject = "Security Alert: Suspicious login attempt",
        preview = "We detected a sign-in attempt from an unrecognized device in Romania. If this wasn't you...",
        time = "Mon",
        isRead = true,
        body = """Important Security Notice,

We detected a suspicious login attempt to your Bank of America account from:
Location: Bucharest, Romania
Device: Unknown Android device
Time: 3:47 AM EST

If this was not you, secure your account immediately:
http://bofa-secure.phishingdomain.ru/verify-identity

Your account may be compromised. Act now.

Bank of America Fraud Prevention"""
    ),
    SampleEmail(
        sender = "Team Updates",
        senderInitial = "T",
        avatarColor = Color(0xFF4285F4),
        subject = "Standup moved to 10am Monday",
        preview = "Hi team, just a reminder that the Monday standup has been moved to 10am instead of 9am...",
        time = "Yesterday",
        isRead = true,
        body = """Hi team,

Just a reminder that the Monday standup has been moved to 10am instead of 9am next week. The meeting room is the same.

No other changes, see you then."""
    )
)