package dev.jakubbalinski.services;

import dev.jakubbalinski.config.MailConfig;
import dev.jakubbalinski.models.DrawResult;
import dev.jakubbalinski.models.Member;
import jakarta.mail.Message;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.recipient.RecipientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SendMailService {
    private static final Logger log = LoggerFactory.getLogger(SendMailService.class);

    private final MailConfig config;
    private final Mailer mailer;

    public SendMailService(MailConfig config) {
        this.config = config;
        this.mailer = buildMailer(config);
    }

    private Mailer buildMailer(MailConfig cfg) {
        return MailerBuilder
                .withSMTPServer(cfg.smtpHost(), cfg.smtpPort(), cfg.smtpUser(), cfg.smtpPassword())
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
    }

    public void sendDrawNotifications(List<DrawResult> results) {

        log.info("Rozpoczynam wysyłkę {} wiadomości...", results.size());

        try {
            for (DrawResult result : results) {
                sendSingleMail(result);
            }
            log.info("Wszystkie wiadomości zostały pomyślnie wysłane!");
        } finally {
            if (mailer != null) {
                mailer.shutdownConnectionPool();
            }
        }
    }

    private void sendSingleMail(DrawResult result) {
        Member giver = result.giver();
        Member receiver = result.receiver();

        Email email = EmailBuilder.startingBlank()
                .from("Organizator Losowania", config.smtpUser())
                .withRecipients(new RecipientBuilder()
                        .withName(giver.name())
                        .withAddress(giver.mail())
                        .withType(Message.RecipientType.TO)
                        .build())
                .withSubject("🎁 Wynik losowania! Zobacz, komu robisz prezent")
                .withPlainText(buildTextMessage(giver, receiver))
                .withHTMLText(buildHtmlMessage(giver, receiver))
                .buildEmail();

        mailer.sendMail(email);
        // Logujemy tylko to, że mail poszedł do dawcy (nie logujemy odbiorcy na produkcji, żeby nie zepsuć sobie niespodzianki!)
        log.info("Wysłano wiadomość do: {} ({})", giver.name(), giver.mail());
    }

    private String buildTextMessage(Member giver, Member receiver) {
        return """
                Hejka naklejka %s! 🎅
                
                Są wyniki naszego super losowania na mikołajki! 🎄
                
                Kupujesz super prezent osobie: %s 🎁
                
                Pamiętaj, że limit to 50 zł!
                
                Powodzenia!
                """.formatted(giver.name(), receiver.name());
    }

    private String buildHtmlMessage(Member giver, Member receiver) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                </head>
                <body style="margin: 0; padding: 0; background-color: #051409; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;">
                
                    <!-- GŁÓWNE TŁO Z EFEKTEM ŚWIĄTECZNEJ NOCY -->
                    <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="background: radial-gradient(circle at top, #1b4332 0%%, #081c15 50%%, #030905 100%%); background-color: #051409; padding: 30px 10px;">
                        <tr>
                            <td align="center">
                
                                <!-- GÓRNY PASEK STATUSU BIEGUNA PÓŁNOCNEGO -->
                                <table width="640" cellpadding="0" cellspacing="0" border="0" style="max-width: 640px; width: 100%%; margin-bottom: 12px;">
                                    <tr>
                                        <td align="center" style="background-color: #000000; color: #38b000; font-family: 'Courier New', monospace; font-size: 11px; padding: 8px 15px; border-radius: 50px; border: 1px solid #ffd700; letter-spacing: 1px; box-shadow: 0 0 15px rgba(255, 215, 0, 0.3);">
                                            🟢 [SATELLITE LINK: ACTIVE] • 🦌 RUDOLPH_GPS: LOCKED • 🔒 SZYFROWANIE: ELF-AES-256
                                        </td>
                                    </tr>
                                </table>
                
                                <!-- GŁÓWNY KONTENER GIGANT -->
                                <table width="640" cellpadding="0" cellspacing="0" border="0" style="max-width: 640px; width: 100%%; background-color: #ffffff; border-radius: 28px; overflow: hidden; box-shadow: 0 0 60px rgba(217, 4, 41, 0.6), 0 25px 60px rgba(0,0,0,0.9); border: 6px solid #ffd700;">
                
                                    <!-- PASEK CANDY CANE #1 -->
                                    <tr>
                                        <td style="background: repeating-linear-gradient(45deg, #d90429, #d90429 15px, #ffffff 15px, #ffffff 30px, #1b4332 30px, #1b4332 45px, #ffd700 45px, #ffd700 60px); height: 22px;"></td>
                                    </tr>
                
                                    <!-- SZPALER ŚWIECĄCYCH LAMPEK CHOINKOWYCH -->
                                    <tr>
                                        <td align="center" style="background-color: #081c15; padding: 12px 10px; border-bottom: 3px solid #ffd700;">
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#ff0055; box-shadow: 0 0 12px #ff0055; margin: 0 8px;"></span>
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#00f5d4; box-shadow: 0 0 12px #00f5d4; margin: 0 8px;"></span>
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#fee440; box-shadow: 0 0 12px #fee440; margin: 0 8px;"></span>
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#00bbf9; box-shadow: 0 0 12px #00bbf9; margin: 0 8px;"></span>
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#ff0055; box-shadow: 0 0 12px #ff0055; margin: 0 8px;"></span>
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#38b000; box-shadow: 0 0 12px #38b000; margin: 0 8px;"></span>
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#fee440; box-shadow: 0 0 12px #fee440; margin: 0 8px;"></span>
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#ff0055; box-shadow: 0 0 12px #ff0055; margin: 0 8px;"></span>
                                            <span style="display:inline-block; width:14px; height:14px; border-radius:50%%; background:#00f5d4; box-shadow: 0 0 12px #00f5d4; margin: 0 8px;"></span>
                                        </td>
                                    </tr>
                
                                    <!-- MEGA BANNER NAGŁÓWKOWY -->
                                    <tr>
                                        <td align="center" style="background: linear-gradient(135deg, #6a040f 0%%, #d90429 50%%, #370617 100%%); background-color: #9b0000; padding: 40px 20px; border-bottom: 6px double #ffd700;">
                                            <div style="font-size: 42px; line-height: 1; margin-bottom: 15px; text-shadow: 0 0 20px #ffffff;">
                                                ❄️ 🦌 🛷 🎅 🎄 🔔 ⛄ ❄️
                                            </div>
                                            <div style="display: inline-block; background-color: #ffd700; color: #6a040f; font-weight: 900; font-size: 12px; padding: 5px 16px; border-radius: 20px; letter-spacing: 3px; text-transform: uppercase; margin-bottom: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.4);">
                                                🚨 PILNA DEPESZA Z BIEGUNA PÓŁNOCNEGO 🚨
                                            </div>
                                            <h1 style="color: #ffffff; margin: 0; font-size: 38px; font-weight: 900; text-transform: uppercase; letter-spacing: 3px; text-shadow: 3px 3px 0px #370617, 0 0 25px #ffd700;">
                                                ✨ MIKOŁAJKOWY ALARM ✨
                                            </h1>
                                            <p style="color: #ffccd5; font-size: 15px; font-weight: bold; margin: 12px 0 0 0; letter-spacing: 2px; text-transform: uppercase;">
                                                ⚡ Protokół Losowania Prezentów Został Wykonany ⚡
                                            </p>
                                        </td>
                                    </tr>
                
                                    <!-- GŁÓWNA ZAWARTOŚĆ -->
                                    <tr>
                                        <td align="center" style="padding: 40px 30px; background: linear-gradient(180deg, #fffdf7 0%%, #fff3e6 100%%); background-color: #fffdf7;">
                
                                            <!-- POWITANIE VIP -->
                                            <div style="font-size: 14px; color: #d90429; font-weight: 900; letter-spacing: 2px; text-transform: uppercase; margin-bottom: 5px;">
                                                ★ Certyfikowany Agent Świąteczny ★
                                            </div>
                                            <h2 style="color: #1b4332; font-size: 34px; margin-top: 0; margin-bottom: 15px; font-weight: 900;">
                                                Hejka naklejka <span style="color: #ffffff; background-color: #d90429; padding: 4px 14px; border-radius: 10px; border: 2px solid #ffd700; box-shadow: 0 4px 10px rgba(217,4,41,0.3); display: inline-block;">%s</span>! 🎅🍪
                                            </h2>
                
                                            <p style="color: #1b4332; font-size: 20px; line-height: 1.5; margin-bottom: 20px; font-weight: 800; background-color: #ffe5ec; padding: 12px 20px; border-radius: 12px; border: 2px dashed #d90429;">
                                                Są wyniki naszego super losowania na mikołaki!!!!!!!!!!! 🎄🦌🔔🔥
                                            </p>
                
                                            <!-- TERMINAL TELEMETRYCZNY ELFÓW -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="margin: 20px 0; background-color: #0d1b2a; border-radius: 14px; border: 3px solid #415a77; overflow: hidden; box-shadow: inset 0 0 20px #000000;">
                                                <tr>
                                                    <td style="background-color: #1b263b; padding: 8px 15px; border-bottom: 1px solid #415a77; font-family: monospace; font-size: 12px; color: #e0e1dd;">
                                                        🔴 🟡 🟢 &nbsp; <strong>root@north-pole-mainframe:~/losowanie# ./run_mikolajki.sh</strong>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td align="left" style="padding: 15px 20px; font-family: 'Courier New', monospace; font-size: 13px; color: #38b000; line-height: 1.6;">
                                                        &gt; Inicjalizacja Kwanto-Bębna Losującego Św. Mikołaja... <span style="color:#ffd700;">[OK]</span><br>
                                                        &gt; Kalibracja nosa Rudolfa i smarowanie płóz... <span style="color:#ffd700;">[100%%]</span><br>
                                                        &gt; Weryfikacja poziomu pierniczków we krwi... <span style="color:#00f5d4;">[WYSOKI]</span><br>
                                                        &gt; Losowanie idealnej pary z pominięciem duplikatów... <span style="color:#ff0055; font-weight:bold;">[BINGO!]</span>
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- PASEK GRZECZNOŚCI (NAUGHTY / NICE METER) -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="margin: 25px 0; background-color: #ffffff; padding: 15px; border-radius: 14px; border: 2px solid #e0e0e0;">
                                                <tr>
                                                    <td align="left" style="font-size: 13px; font-weight: bold; color: #1b4332; padding-bottom: 8px; text-transform: uppercase;">
                                                        📊 Twój tegoroczny indeks grzeczności wg Mikołaja: <span style="color: #d90429;">99.9%% (VIP)</span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <div style="width: 100%%; background-color: #e9ecef; border-radius: 20px; height: 22px; overflow: hidden; border: 2px solid #1b4332;">
                                                            <div style="width: 96%%; height: 22px; background: repeating-linear-gradient(45deg, #2b9348, #2b9348 10px, #55a630 10px, #55a630 20px); text-align: right; color: #ffffff; font-size: 11px; line-height: 22px; font-weight: bold; padding-right: 10px;">
                                                                🌟 WZOROWY ŚWIĄTECZNY AGENT 🌟
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- ZŁOTY BILET (GOLDEN TICKET) Z WYLOSOWANĄ OSOBĄ -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="margin: 30px 0;">
                                                <tr>
                                                    <td align="center" style="background: linear-gradient(135deg, #ffe169 0%%, #fff8d6 35%%, #ffd700 70%%, #dba111 100%%); background-color: #ffd700; border: 5px dashed #9b0000; border-radius: 24px; padding: 35px 20px; box-shadow: 0 15px 35px rgba(217, 4, 41, 0.35);">
                
                                                        <div style="background-color: #9b0000; color: #ffd700; display: inline-block; padding: 6px 18px; border-radius: 50px; font-size: 13px; font-weight: 900; letter-spacing: 3px; text-transform: uppercase; margin-bottom: 15px;">
                                                            🎫 OFICJALNY ZŁOTY BILET MIKOŁAJKOWY 🎫
                                                        </div>
                
                                                        <div style="font-size: 16px; color: #590000; text-transform: uppercase; letter-spacing: 2px; font-weight: 900; margin-bottom: 12px;">
                                                            🥁 WERBLE PROSZĘ... KUPUJESZ SUPER PREZENT OSOBIE: 🥁
                                                        </div>
                
                                                        <!-- SUPER BOX Z IMIENIEM -->
                                                        <div style="font-size: 44px; font-weight: 900; color: #ffffff; background: linear-gradient(90deg, #800f2f, #d90429, #800f2f); background-color: #d90429; margin: 15px 0; padding: 22px 35px; border-radius: 18px; border: 4px solid #ffffff; text-shadow: 3px 3px 0px #370617, 0 0 20px #ffd700; box-shadow: 0 10px 25px rgba(0,0,0,0.45); display: inline-block; letter-spacing: 2px;">
                                                            🎁 %s 🎁
                                                        </div>
                
                                                        <div style="font-size: 28px; margin-top: 12px; margin-bottom: 15px;">
                                                            🎉 🎊 🥳 🎄 🦌 ✨ 🎁 🥂 🎆
                                                        </div>
                
                                                        <!-- FAKE KOD KRESKOWY KUPONU -->
                                                        <div style="background-color: #ffffff; padding: 10px 20px; border-radius: 8px; display: inline-block; border: 1px solid #9b0000; margin-top: 5px;">
                                                            <div style="font-family: monospace; font-size: 20px; letter-spacing: 3px; color: #000000; line-height: 1;">
                                                                ║▌║█║▌│║▌║▌█║▌║█║▌│║
                                                            </div>
                                                            <div style="font-family: monospace; font-size: 10px; color: #555555; letter-spacing: 4px; margin-top: 4px;">
                                                                SANTA-AUTH-2026-VIP-PASS
                                                            </div>
                                                        </div>
                
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- MEGA BANNER Z LIMITEM 50 ZŁ -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="margin: 25px 0;">
                                                <tr>
                                                    <td align="center" style="background: linear-gradient(135deg, #1b4332 0%%, #081c15 100%%); background-color: #1b4332; color: #ffffff; padding: 22px 25px; border-radius: 20px; border: 4px solid #ffd700; box-shadow: 0 10px 25px rgba(27, 67, 50, 0.5);">
                                                        <div style="font-size: 13px; color: #a3b18a; text-transform: uppercase; letter-spacing: 3px; font-weight: bold; margin-bottom: 5px;">
                                                            🏦 DYREKTYWA BUDŻETOWA SKARBCA ELFÓW 🏦
                                                        </div>
                                                        <div style="font-size: 26px; font-weight: 900; letter-spacing: 1px;">
                                                            💰 Pamietajj limit to: <span style="color: #ffd700; font-size: 34px; text-decoration: underline;">50 zł</span>! 💸
                                                        </div>
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- TABELA TAKTYCZNA MISJI -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="margin: 30px 0; border-collapse: collapse; border-radius: 16px; overflow: hidden; border: 3px solid #d90429;">
                                                <tr>
                                                    <td colspan="2" align="center" style="background-color: #d90429; color: #ffffff; font-weight: 900; padding: 12px; font-size: 15px; letter-spacing: 2px; text-transform: uppercase;">
                                                        📋 KARTA TAKTYCZNA TWOJEJ MISJI 📋
                                                    </td>
                                                </tr>
                                                <tr style="background-color: #ffffff; border-bottom: 1px solid #eee;">
                                                    <td style="padding: 12px 15px; font-weight: bold; color: #1b4332; font-size: 14px; width: 45%%;">🕵️ Kryptonim Operacji:</td>
                                                    <td style="padding: 12px 15px; color: #d90429; font-weight: 900; font-size: 14px;">MIKOŁAKI ULTRA SECRET</td>
                                                </tr>
                                                <tr style="background-color: #fff8f8; border-bottom: 1px solid #eee;">
                                                    <td style="padding: 12px 15px; font-weight: bold; color: #1b4332; font-size: 14px;">🎯 Cel Prezentowy:</td>
                                                    <td style="padding: 12px 15px; color: #000000; font-weight: 900; font-size: 14px;">100%% Trafiony Prezent</td>
                                                </tr>
                                                <tr style="background-color: #ffffff; border-bottom: 1px solid #eee;">
                                                    <td style="padding: 12px 15px; font-weight: bold; color: #1b4332; font-size: 14px;">🤐 Poziom Tajności:</td>
                                                    <td style="padding: 12px 15px; color: #800f2f; font-weight: bold; font-size: 14px;">NATO COSMIC TOP SECRET</td>
                                                </tr>
                                                <tr style="background-color: #fff8f8;">
                                                    <td style="padding: 12px 15px; font-weight: bold; color: #1b4332; font-size: 14px;">⚠️ Kara za wygadanie się:</td>
                                                    <td style="padding: 12px 15px; color: #333333; font-size: 14px;">Worek węgla + słuchanie "Last Christmas" przez 24h 🎶</td>
                                                </tr>
                                            </table>
                
                                            <!-- INSTRUKCJA KROK PO KROKU -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="background-color: #e8f5e9; border: 2px solid #2b9348; border-radius: 16px; padding: 20px; margin-bottom: 25px;">
                                                <tr>
                                                    <td align="left" style="color: #1b4332; font-size: 14px; line-height: 1.8;">
                                                        <div style="font-weight: 900; font-size: 16px; margin-bottom: 8px; text-transform: uppercase; color: #1b4332;">
                                                            🛷 Jak wykonać misję bez przypału:
                                                        </div>
                                                        <strong>KROK 1:</strong> Przeczytaj imię z biletu i zrób poker face  pokerowego mistrza 🗿<br>
                                                        <strong>KROK 2:</strong> Przeprowadź dyskretny wywiad środowiskowy (lub użyj szóstego zmysłu) 🔍<br>
                                                        <strong>KROK 3:</strong> Upoluj coś epickiego w limicie <strong>50 zł</strong> 🛍️<br>
                                                        <strong>KROK 4:</strong> Zapakuj prezent w najbardziej szeleszczący papier świata 🎀
                                                    </td>
                                                </tr>
                                            </table>
                
                                            <!-- OSTRZEŻENIE ANTY-SPOILEROWE -->
                                            <div style="background-color: #ffe5ec; border: 3px solid #d90429; padding: 18px 20px; border-radius: 14px; margin-top: 20px; color: #800f2f; font-size: 15px; box-shadow: 0 5px 15px rgba(217,4,41,0.15);">
                                                🚨 <strong>UWAGA! KLAUZULA TAJNOŚCI:</strong> 🚨<br>
                                                Nie pokazuj nikomu tego maila! Nawet jeśli ktoś przekupi Cię mandarynkami, piernikami albo gorącą czekoladą – trzymaj język za zębami aż do dnia rozdania prezentów! 🍊🍫🤫
                                            </div>
                
                                            <!-- PIECZĘĆ I PODPIS MIKOŁAJA -->
                                            <table width="100%%" cellpadding="0" cellspacing="0" border="0" style="margin-top: 35px;">
                                                                                                    <tr>
                                                                                                        <td align="center">
                                                                                                            <!-- Zewnętrzna tabela-pieczęć o sztywnej szerokości 140px, żeby się nie rozjechała -->
                                                                                                            <table width="140" height="140" cellpadding="0" cellspacing="0" border="0" style="width: 140px; height: 140px; border-radius: 50%%; background: radial-gradient(circle, #ffd700 0%%, #dba111 100%%); border: 4px double #9b0000; box-shadow: 0 8px 20px rgba(0,0,0,0.3);">
                                                                                                                <tr>
                                                                                                                    <td align="center" valign="middle" style="text-align: center; vertical-align: middle; padding: 10px;">
                                                                                                                        <div style="color: #9b0000; font-weight: 900; font-size: 11px; text-transform: uppercase; line-height: 1.3;">
                                                                                                                            ⭐ OFICJALNA ⭐<br>PIECZĘĆ<br>MIKOŁAJA
                                                                                                                        </div>
                                                                                                                    </td>
                                                                                                                </tr>
                                                                                                            </table>
                
                                                                                                            <h3 style="color: #d90429; font-size: 30px; margin-top: 25px; margin-bottom: 5px; text-transform: uppercase; letter-spacing: 2px; font-weight: 900;">
                                                                                                                🚀 Powodzenia! 🛍️🎄
                                                                                                            </h3>
                                                                                                            <div style="font-size: 28px; margin-top: 5px;">
                                                                                                                🎅🤜🤛🦌 🔥🎁🔥
                                                                                                            </div>
                                                                                                        </td>
                                                                                                    </tr>
                                                                                                </table>
                
                                        </td>
                                    </tr>
                
                                    <!-- MEGA STOPKA -->
                                    <tr>
                                        <td align="center" style="background-color: #051409; color: #a3b18a; padding: 30px 20px; font-size: 12px; border-top: 6px solid #ffd700; line-height: 1.6;">
                                            <div style="font-size: 26px; margin-bottom: 12px; letter-spacing: 4px;">
                                                🎄🎁❄️⛄🦌🛷🔔🕯️🍪🥛🧦🌟
                                            </div>
                                            <strong style="color: #ffd700; font-size: 14px; text-transform: uppercase; letter-spacing: 1px;">
                                                Wygenerowano przez Ultra-Świąteczną Maszynę Losującą™ v2137 PRO MAX
                                            </strong><br>
                                            Departament Logistyki Prezentowej Św. Mikołaja • Rovaniemi, Laponia • Sektor Komina #7<br>
                                            <span style="color: #52796f; font-size: 11px;">
                                                Ten mail ulegnie samozniszczeniu po zjedzeniu dwunastu pierogów. Wesołych Mikołajek!
                                            </span>
                                        </td>
                                    </tr>
                
                                    <!-- PASEK CANDY CANE #2 -->
                                    <tr>
                                        <td style="background: repeating-linear-gradient(45deg, #d90429, #d90429 15px, #ffffff 15px, #ffffff 30px, #1b4332 30px, #1b4332 45px, #ffd700 45px, #ffd700 60px); height: 22px;"></td>
                                    </tr>
                
                                </table>
                
                            </td>
                        </tr>
                    </table>
                
                </body>
                </html>
                """.formatted(giver.name(), receiver.name());
    }
}
