package com.example.model

data class ExamRuleInfo(
    val examName: String,
    val hindiName: String,
    val speedRequirement: String,
    val durationText: String,
    val backspaceAllowed: Boolean,
    val backspaceRuleEn: String,
    val backspaceRuleHi: String,
    val errorPenaltyEn: String,
    val errorPenaltyHi: String,
    val importantNotesEn: String,
    val importantNotesHi: String
)

data class ExamParagraph(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val examTag: String, // "BSF HCM", "SSC CGL", "SSC CHSL", "CRPF HCM", "HIGH COURT", "EDITORIAL"
    val category: String, // "Government Exams", "Force Exams (No Backspace)", "Court Judgments", "Editorials"
    val durationMinutes: Int,
    val targetWpm: Int,
    val allowBackspace: Boolean,
    val wordCount: Int,
    val passageText: String,
    val keyPointsEn: String,
    val keyPointsHi: String
)

object ExamRuleGuide {
    val officialRules: List<ExamRuleInfo> = listOf(
        ExamRuleInfo(
            examName = "BSF HCM (Head Constable Ministerial)",
            hindiName = "बीएसएफ एचसीएम (हेड कांस्टेबल मिनिस्ट्रियल)",
            speedRequirement = "35 WPM (English) / 30 WPM (Hindi)",
            durationText = "10 Minutes (1050 keystrokes / ~350 words minimum)",
            backspaceAllowed = false,
            backspaceRuleEn = "STRICTLY NO BACKSPACE ALLOWED! Backspace key is completely disabled or penalized.",
            backspaceRuleHi = "बैकस्पेस बिल्कुल वर्जित है! बैकस्पेस की अनुमति नहीं होती है और गलती सुधारी नहीं जा सकती।",
            errorPenaltyEn = "Max 5% mistakes allowed. Beyond 5% mistakes, 10 words deducted for each mistake.",
            errorPenaltyHi = "अधिकतम 5% गलतियों की छूट। 5% से अधिक गलती होने पर प्रत्येक गलती पर 10 शब्द काटे जाते हैं।",
            importantNotesEn = "Type accurately without looking at keyboard. Since correction is impossible, deliberate typing is key.",
            importantNotesHi = "सटीकता सबसे महत्वपूर्ण है क्योंकि गलती मिटाने का कोई विकल्प नहीं मिलता।"
        ),
        ExamRuleInfo(
            examName = "SSC CGL (DEST - Data Entry Skill Test)",
            hindiName = "एसएससी सीजीएल (डाटा एंट्री स्किल टेस्ट)",
            speedRequirement = "27 WPM (2000 key depressions in 15 mins) / 35 WPM",
            durationText = "15 Minutes (approx 2000 keystrokes)",
            backspaceAllowed = true,
            backspaceRuleEn = "Backspace is allowed for corrections within the 15-minute test window.",
            backspaceRuleHi = "बैकस्पेस की अनुमति है। आप दिए गए 15 मिनट में गलतियों को सुधार सकते हैं।",
            errorPenaltyEn = "Full errors (omission, substitution, addition of words) and Half errors (spacing, punctuation, spelling). Cutoff varies by category (5% to 7%).",
            errorPenaltyHi = "पूर्ण त्रुटियाँ (शब्द छूटना या बदलना) तथा अर्ध त्रुटियाँ (स्पेसिंग, विराम चिह्न)। 5% से 7% तक छूट।",
            importantNotesEn = "Master formatting and complete the passage before time to review errors.",
            importantNotesHi = "समय से 2 मिनट पहले पैसेज पूरा करके एक बार स्पेलिंग जांचना सर्वोत्तम रणनीति है।"
        ),
        ExamRuleInfo(
            examName = "SSC CHSL (LDC / JSA / DEO)",
            hindiName = "एसएससी सीएचएसएल (लोअर डिवीजन क्लर्क / डीईओ)",
            speedRequirement = "35 WPM (English) / 30 WPM (Hindi)",
            durationText = "10 Minutes (~1750 key depressions)",
            backspaceAllowed = true,
            backspaceRuleEn = "Backspace is permitted in standard SSC test interface.",
            backspaceRuleHi = "एसएससी के मानक परीक्षा सॉफ्टवेयर में बैकस्पेस की अनुमति होती है।",
            errorPenaltyEn = "Permissible error limit: UR: 7%, Reserved categories: 10%.",
            errorPenaltyHi = "सामान्य वर्ग के लिए अधिकतम 7% तथा आरक्षित वर्गों के लिए 10% गलतियाँ मान्य हैं।",
            importantNotesEn = "Speed with 95%+ accuracy guarantees qualification.",
            importantNotesHi = "35 WPM की गति 95% से अधिक सटीकता के साथ होने पर चयन सुनिश्चित होता है।"
        ),
        ExamRuleInfo(
            examName = "CRPF HCM / CISF HCM",
            hindiName = "सीआरपीएफ / सीआईएसएफ एचसीएम",
            speedRequirement = "35 WPM (English) / 30 WPM (Hindi)",
            durationText = "10 Minutes (Strict official timer)",
            backspaceAllowed = false,
            backspaceRuleEn = "Restricted or No Backspace in force typing software. Practice without correction.",
            backspaceRuleHi = "फोर्स टाइपिंग टेस्ट में बैकस्पेस प्रतिबंधित होता है। बिना बैकस्पेस अभ्यास अनिवार्य है।",
            errorPenaltyEn = "5% error margin. Substantial mark deduction beyond threshold.",
            errorPenaltyHi = "5% से अधिक गलतियों पर भारी अंक कटौती होती है।",
            importantNotesEn = "Focus on word-by-word cadence without rushing.",
            importantNotesHi = "घबराहट से बचें और स्थिर लय में टाइप करें।"
        ),
        ExamRuleInfo(
            examName = "High Court & District Court Typist",
            hindiName = "उच्च न्यायालय एवं जिला न्यायालय टाइपिस्ट",
            speedRequirement = "35 to 40 WPM",
            durationText = "10 Minutes / 15 Minutes Legal Matter",
            backspaceAllowed = true,
            backspaceRuleEn = "Backspace allowed but high penalties for capitalization and legal terms.",
            backspaceRuleHi = "बैकस्पेस मान्य है किंतु कानूनी शब्दावली और बड़े अक्षरों की गलतियों पर भारी दंड होता है।",
            errorPenaltyEn = "Stringent formatting and case sensitivity checks.",
            errorPenaltyHi = "कैपिटल लेटर, एक्ट (Acts) और सेक्शन (Sections) सही लिखना आवश्यक है।",
            importantNotesEn = "Practice specialized legal judgments, petitions, and legal vocabulary.",
            importantNotesHi = "न्यायालय के निर्णयों और कानूनी प्रारूपों का विशेष अभ्यास करें।"
        )
    )
}

object ExamParagraphCatalog {
    val paragraphs: List<ExamParagraph> = listOf(
        // 1. BSF HCM Special (No Backspace)
        ExamParagraph(
            id = "bsf_hcm_01",
            titleEn = "BSF HCM Official Mock 1: Border Security & National Vigilance",
            titleHi = "बीएसएफ एचसीएम मॉक 1: सीमा सुरक्षा एवं राष्ट्रीय सतर्कता",
            examTag = "BSF HCM",
            category = "Force Exams (No Backspace)",
            durationMinutes = 10,
            targetWpm = 35,
            allowBackspace = false,
            wordCount = 370,
            keyPointsEn = "NO BACKSPACE ALLOWED. Aim for 35+ WPM with under 5% errors.",
            keyPointsHi = "बैकस्पेस पूर्णतः बंद है। 35+ WPM गति और 5% से कम गलतियों का लक्ष्य रखें।",
            passageText = "The Border Security Force is the primary border guarding organisation of India. Established in the wake of the nineteen sixty five war for ensuring the security of the borders of India, it is one of the largest border patrol forces in the world. The personnel of this elite force operate under severe climatic conditions ranging from scorching desert temperatures in Rajasthan to freezing glacial terrains in the northern frontiers and dense marshlands of the Sundarbans. In administrative roles such as Head Constable Ministerial, efficiency in clerical duties, meticulous record management, and rapid documentation are paramount. Official communication requires strict precision, clear vocabulary, and disciplined speed. When typing administrative correspondence or military dispatches, every character counts. Candidates appearing for departmental and direct recruitment typing tests must realize that touching thirty five words per minute without error is a product of disciplined posture and continuous muscle memory. Unlike civilian office software where frequent backspacing is normalized, force examinations demand complete mental focus so that errors are prevented before the key is struck. Cultivating calm breathing, upright posture, and keeping your eyes fixed firmly on the source text guarantees that fingers strike the home keys with unshakeable accuracy."
        ),

        // 2. BSF HCM Special 2 (No Backspace)
        ExamParagraph(
            id = "bsf_hcm_02",
            titleEn = "BSF HCM Official Mock 2: Modernization of Defence Logistics",
            titleHi = "बीएसएफ एचसीएम मॉक 2: रक्षा लॉजिस्टिक्स का आधुनिकीकरण",
            examTag = "BSF HCM",
            category = "Force Exams (No Backspace)",
            durationMinutes = 10,
            targetWpm = 35,
            allowBackspace = false,
            wordCount = 360,
            keyPointsEn = "Strict force examination test. Backspace is disabled.",
            keyPointsHi = "कठिन परीक्षा प्रारूप। बैकस्पेस बंद है, सुधार की अनुमति नहीं है।",
            passageText = "Modern defence administration relies heavily on digitized inventory networks, real time logistics, and streamlined communication channels across forward battalions. Quick transmission of inventory data, movement orders, ration registers, and personnel records requires clerical operators to process vast amounts of technical information at superior typing speeds. Accuracy remains the foremost standard because an erroneous dispatch code or misstated stock balance can cause operational friction across remote outposts. Aspiring candidates must practice touch typing regularly on standard keyboards with uniform key resistance. Always remember the home row positions: fingers resting effortlessly on ASDF and JKL. Do not look down at the keyboard even for a fraction of a second. Trust the tactile bumps on F and J to guide your hands back to baseline. By training yourself to type steadily without relying on the backspace key, your mind learns to anticipate each letter rhythmically. Continuous daily drills transform hesitant fingers into disciplined instruments of operational efficiency."
        ),

        // 3. SSC CGL DEST (15 Minutes)
        ExamParagraph(
            id = "ssc_cgl_01",
            titleEn = "SSC CGL DEST Mock 1: Economic Reforms & Fiscal Policy",
            titleHi = "एसएससी सीजीएल डीईएसटी मॉक 1: आर्थिक सुधार एवं राजकोषीय नीति",
            examTag = "SSC CGL",
            category = "Government Exams",
            durationMinutes = 15,
            targetWpm = 30,
            allowBackspace = true,
            wordCount = 450,
            keyPointsEn = "15-minute standard DEST test. Backspace enabled. Watch for punctuation & numbers.",
            keyPointsHi = "15-मिनट मानक डीईएसटी। बैकस्पेस चालू है। विराम चिह्न व वर्तनी पर ध्यान दें।",
            passageText = "Fiscal policy and structural economic reforms play an instrumental role in shaping the socioeconomic landscape of developing nations. Over recent decades, the modernization of taxation systems, public expenditure management, and digital payment infrastructure has fostered unprecedented transparency across government departments. The implementation of unified indirect taxation alongside direct benefit transfers has reduced bureaucratic leaks and expanded the national tax base. Effective governance demands that economic analysts and administrative staff maintain robust digital record systems with high data fidelity. A key element of government clerical recruitment through competitive examinations like the Staff Selection Commission is assessing typing accuracy under time constraints. Data Entry Skill Tests are structured to evaluate candidate capacity to reproduce comprehensive economic and demographic texts with minimal errors. Typing at twenty seven to thirty words per minute with fewer than seven percent mistakes ensures compliance with official standards. Typists should maintain a steady speed rather than bursts of frantic typing that invite avoidable spelling blunders. Always reserve two minutes at the conclusion of the test to review proper nouns, numerical figures, and comma placements."
        ),

        // 4. SSC CHSL (10 Minutes)
        ExamParagraph(
            id = "ssc_chsl_01",
            titleEn = "SSC CHSL Mock 1: Digital India & Public Service Delivery",
            titleHi = "एसएससी सीएचएसएल मॉक 1: डिजिटल इंडिया एवं लोक सेवा वितरण",
            examTag = "SSC CHSL",
            category = "Government Exams",
            durationMinutes = 10,
            targetWpm = 35,
            allowBackspace = true,
            wordCount = 375,
            keyPointsEn = "Target: 35 WPM English. Backspace allowed for corrections.",
            keyPointsHi = "लक्ष्य: 35 WPM अंग्रेजी। बैकस्पेस सुधार के लिए उपलब्ध है।",
            passageText = "The Digital India initiative represents a monumental paradigm shift in public administration and citizen empowerment. By connecting rural gram panchayats through high speed optical fiber networks and providing digital identities to over one billion citizens, essential services have reached grassroots communities with unmatched speed. Government schemes covering financial inclusion, healthcare accessibility, agricultural subsidies, and educational scholarships are now processed electronically. In government secretariats, Lower Division Clerks and Junior Secretarial Assistants act as the backbone of departmental documentation. Typing speed and accuracy are crucial metrics for qualifying these examinations. Typists who master the thirty five words per minute benchmark can generate correspondence, file notes, and gazette notifications swiftly. Practicing with real examination passages enhances finger dexterity, keyboard familiarity, and overall concentration. Focus on smooth keystrokes, keep your wrists slightly elevated above the desk, and maintain consistent pace from the opening sentence to the final paragraph."
        ),

        // 5. CRPF / CISF HCM (No Backspace)
        ExamParagraph(
            id = "crpf_hcm_01",
            titleEn = "CRPF / CISF HCM Mock: Internal Security & Disaster Management",
            titleHi = "सीआरपीएफ / सीआईएसएफ मॉक: आंतरिक सुरक्षा एवं आपदा प्रबंधन",
            examTag = "CRPF HCM",
            category = "Force Exams (No Backspace)",
            durationMinutes = 10,
            targetWpm = 35,
            allowBackspace = false,
            wordCount = 360,
            keyPointsEn = "Zero backspace tolerance. 35 WPM speed test simulation.",
            keyPointsHi = "बैकस्पेस बंद। 35 WPM परीक्षा सिमुलेशन।",
            passageText = "Paramilitary forces form the vanguard of internal security and civilian protection during national crises, civil emergencies, and natural disasters. From managing riot control and safeguarding vital public installations to mounting rapid rescue operations during floods and earthquakes, these uniformed personnel embody supreme courage and civic devotion. Behind every field deployment stands a highly organized ministerial staff responsible for drafting situation reports, casualty evacuations, supply requisitions, and central command dispatches. Typing tests for Head Constable Ministerial in central armed police forces are among the most stringent in the nation because candidates must type with absolute accuracy on custom software where corrections are disallowed. To succeed under such unforgiving exam conditions, students must train their mind to read two or three words ahead while the fingers type the preceding phrase smoothly. Do not panic if a minor spelling error occurs; maintain your cadence and continue typing with unperturbed composure until the clock expires."
        ),

        // 6. High Court & Legal Clerk
        ExamParagraph(
            id = "court_01",
            titleEn = "High Court Typist Mock: Constitutional Jurisprudence & Judicial Review",
            titleHi = "उच्च न्यायालय टाइपिस्ट मॉक: संवैधानिक विधि एवं न्यायिक समीक्षा",
            examTag = "HIGH COURT",
            category = "Court Judgments",
            durationMinutes = 10,
            targetWpm = 40,
            allowBackspace = true,
            wordCount = 380,
            keyPointsEn = "Legal terminology, Section citations, and high speed 40 WPM.",
            keyPointsHi = "कानूनी शब्दावली, धाराएं एवं 40 WPM उच्च गति परीक्षा।",
            passageText = "The doctrine of judicial review constitutes an indispensable feature of constitutional democracy, empowering higher courts to evaluate the validity of legislative enactments and executive actions against fundamental rights. In landmark jurisprudence delivered across several decades, the Supreme Court and High Courts have consistently held that the rule of law guarantees equality before the law and protection against arbitrary state action under Articles fourteen, nineteen, and twenty one. Court typists and judicial stenographers occupy a critical position in this apparatus by recording witness testimonies, daily court proceedings, bail orders, and detailed judgments with uncompromising exactitude. Legal typing demands familiarity with legal Latin terms such as prima facie, certiorari, mandamus, and habeas corpus alongside rigorous adherence to capitalized words, quotation marks, and statutory section references. Building typing proficiency for judicial examinations necessitates practicing authentic judgment texts, refining punctuation agility, and typing complex sentence structures without loss of speed."
        ),

        // 7. Editorial & Current Affairs
        ExamParagraph(
            id = "editorial_01",
            titleEn = "Editorial: Artificial Intelligence & The Future of Global Employment",
            titleHi = "संपादकीय: कृत्रिम बुद्धिमत्ता एवं वैश्विक रोजगार का भविष्य",
            examTag = "EDITORIAL",
            category = "Editorials",
            durationMinutes = 10,
            targetWpm = 35,
            allowBackspace = true,
            wordCount = 390,
            keyPointsEn = "Rich contemporary vocabulary, sophisticated sentence structures.",
            keyPointsHi = "समसामयिक शब्दावली, वैचारिक विश्लेषण एवं गति अभ्यास।",
            passageText = "The rapid evolution of artificial intelligence, automated reasoning, and machine learning models has initiated a profound transition across the global labor economy. While algorithmic systems demonstrate remarkable proficiency in processing colossal datasets, synthesizing medical scans, and automating routine computational tasks, human cognitive attributes such as empathy, creative intuition, and complex ethical judgment remain irreplaceable. Educational institutions and policy makers must recalibrate traditional curricula to emphasize digital literacy, critical thinking, and adaptable technical skills. Students who learn computer fundamentals, touch typing, and digital tools at an early age possess a distinct competitive advantage in navigating modern workplaces. Fast and accurate keyboarding is not merely a technical skill; it functions as a cognitive bridge connecting conscious thought with immediate digital representation. Practicing diverse editorial passages broadens vocabulary, refines grammatical sensitivity, and sharpens typing stamina for competitive examinations and lifelong productivity."
        ),

        // 8. SSC Steno / Banking
        ExamParagraph(
            id = "bank_clerk_01",
            titleEn = "Bank & Clerical Mock: Financial Inclusion & Digital Banking Innovations",
            titleHi = "बैंक एवं क्लर्क मॉक: वित्तीय समावेशन एवं डिजिटल बैंकिंग नवाचार",
            examTag = "SSC STENO",
            category = "Government Exams",
            durationMinutes = 10,
            targetWpm = 35,
            allowBackspace = true,
            wordCount = 370,
            keyPointsEn = "Banking clerical standard passage with financial terminology.",
            keyPointsHi = "बैंकिंग शब्दावली और सामान्य लिपिकीय परीक्षा प्रारूप।",
            passageText = "Financial inclusion serves as a vital pillar for balanced socioeconomic progress and poverty eradication in emerging markets. The expansion of mobile banking applications, unified payment interfaces, and microfinance networks has enabled unbanked rural populations to participate actively in modern commercial ecosystems. Small entrepreneurs, farmers, and self help groups can now secure formal credit, access insurance products, and deposit savings securely from their handheld devices. In public sector banks and state commercial institutions, clerical cadres manage transactions, customer service requests, and loan documentation on specialized core banking solutions. Candidates preparing for clerical and administrative recruitment must develop swift keyboarding reflexes and meticulous typing accuracy to handle high customer volumes with minimal clerical error. Regular practice with comprehensive banking texts solidifies confidence and ensures success during competitive typing and computer proficiency tests."
        )
    )

    fun search(query: String, filterExam: String = "ALL"): List<ExamParagraph> {
        val q = query.trim().lowercase()
        return paragraphs.filter { item ->
            val matchesFilter = when (filterExam.uppercase()) {
                "ALL" -> true
                "BSF" -> item.examTag.contains("BSF", ignoreCase = true) || !item.allowBackspace
                "SSC" -> item.examTag.contains("SSC", ignoreCase = true)
                "COURT" -> item.examTag.contains("COURT", ignoreCase = true)
                "NO_BACKSPACE" -> !item.allowBackspace
                else -> item.examTag.equals(filterExam, ignoreCase = true)
            }

            val matchesQuery = if (q.isEmpty()) {
                true
            } else {
                item.titleEn.lowercase().contains(q) ||
                item.titleHi.lowercase().contains(q) ||
                item.examTag.lowercase().contains(q) ||
                item.category.lowercase().contains(q) ||
                item.passageText.lowercase().contains(q)
            }

            matchesFilter && matchesQuery
        }
    }
}
