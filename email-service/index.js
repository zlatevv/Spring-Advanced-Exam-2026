require('dotenv').config();
const express = require('express');
const nodemailer = require('nodemailer');

const app = express();
app.use(express.json());

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: process.env.GMAIL_USER,
        pass: process.env.GMAIL_APP_PASSWORD,
    },
});

app.post('/send-reset-email', async (req, res) => {
    const { email, resetToken } = req.body;

    if (!email || !resetToken) {
        return res.status(400).json({ message: 'email and resetToken are required.' });
    }

    const resetLink = `${process.env.FRONTEND_URL || 'http://localhost'}/reset-password?token=${resetToken}`;

    try {
        await transporter.sendMail({
            from: process.env.GMAIL_USER,
            to: email,
            subject: 'Reset your RareManuscripts password',
            html: `<p>Click below to reset your password. This link expires in 15 minutes.</p>
                   <a href="${resetLink}">${resetLink}</a>`,
        });
        res.status(200).json({ message: 'Email sent.' });
    } catch (err) {
        console.error('Failed to send email:', err);
        res.status(500).json({ message: 'Failed to send email.' });
    }
});

app.listen(process.env.PORT || 3001, () => {
    console.log(`Email service listening on port ${process.env.PORT || 3001}`);
});