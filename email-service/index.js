require('dotenv').config();
const express = require('express');
const { Resend } = require('resend');

const app = express();
app.use(express.json());

const resend = new Resend(process.env.RESEND_API_KEY);

app.post('/send-reset-email', async (req, res) => {
    const { email, resetToken } = req.body;

    if (!email || !resetToken) {
        return res.status(400).json({ message: 'email and resetToken are required.' });
    }

    const resetLink = `${process.env.FRONTEND_URL || 'http://localhost'}/reset-password?token=${resetToken}`;

    try {
        await resend.emails.send({
            from: 'Rare Manuscripts Support <onboarding@resend.dev>',
            to: email,
            subject: 'Reset your RareManuscripts password',
            html: `<p>Click below to reset your password. This link expires in 15 minutes.</p>
                   <a href="${resetLink}">${resetLink}</a>`,
        });

        res.status(200).json({ message: 'Email sent successfully.' });
    } catch (err) {
        console.error('Failed to send email:', err);
        res.status(500).json({ message: 'Failed to send email.', error: err.message });
    }
});

app.listen(process.env.PORT || 3001, () => {
    console.log(`Email service listening on port ${process.env.PORT || 3001}`);
});