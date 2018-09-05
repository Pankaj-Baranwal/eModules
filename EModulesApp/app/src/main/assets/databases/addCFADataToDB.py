import codecs
from random import randint
import sqlite3


def save_to_db(data):
    global topics
    lookup = [18, 32, 44, 68, 76, 88, 94, 106, 112, 120]
    conn = sqlite3.connect('questionsdb.db')
    conn.execute('CREATE TABLE IF NOT EXISTS CFA_questions (ID INTEGER PRIMARY KEY AUTOINCREMENT,query TEXT NOT '
                 'NULL, solution TEXT NOT NULL, correct TEXT, topic TEXT, notes TEXT, marked TEXT, time_txt TEXT, '
                 'flagged INT);')
    for i in range((len(data) - 1) // 3):
        variable = 'INSERT or REPLACE INTO CFA_questions (query,solution,correct,topic) VALUES (?, ?, ?, ?)'
        if lookup[0]*3 <= i:
            print(i)
            loopup = lookup[1:]
            topics = topics[1:]
        where = data[i * 3 + 1].index("Answer:") + 7
        if data[i * 3 + 1][where] == " ":
            where += 1
        conn.execute(variable, [data[i * 3], data[i * 3 + 2], data[i * 3 + 1][where], topics[0]])
    conn.commit()
    conn.close()


with codecs.open('Mock 5A Solutions.htm', "r", encoding='utf-8', errors='ignore') as f:
    page = f.readlines()
topics = [ "Ethical and Professional Standards", "Quantitative Methods", "Economics", "Financial Reporting and Analysis", "Corporate Finance", "Equity Investments", "Derivative Investments", "Fixed Income Investments", "Alternative Investments", "Portfolio Management"]
data = ""
for line in page:
    data = data + line.strip()

data = data[data.index("<body"):]
data = data.split('$%^')
save_to_db(data)

