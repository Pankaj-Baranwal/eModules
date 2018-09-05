# Database Details 



> Table name is ```questions```.

## Schema

|Column Name|Data Type|Key|NULL or NOT|Remakrs|
|---|---|---|---|---|
|ID | INT | PRIMARY KEY | NOT NULL|Question number|
|query | TEXT | | NOT NULL|The content of the question part of the question including possible answer choices (HTML*)|
|solution | TEXT | | NOT NULL|Explanation of the answer (HTML*)|
|correct | TEXT | | |Correct answer choice ('a'/'b'/'c'/'d'/'e')|
|topic | TEXT | | |Which topic question belongs to (five possible topics in this table)|
|notes | TEXT | | |Notes associated with this question via app (NULL if none)|
|marked | TEXT | | |What the user marked as his answer ('a'/'b'/'c'/'d'/'e') (NULL if unattempted)|
|time_txt | TEXT | | |Time taken by user to answer in text format (NULL if unattempted)|
|flagged | INT | | |Whether user flagged it, marked as doubt (0/1)|

\* => TEXT is in HTML format and includes images in base64 format. So may use webview to render this html content

## Sample row
|ID|query|solution|correct|topic|notes|marked|time_txt|flagged|
|-|-|-|-|-|-|-|-|-|
"1"|	"<p><b>Book Question: 1</b></p><p>The price of a coat in a certain store is $500. If the price of the coat is to be       reduced by $150, by what percent is the price to be reduced?    </p><div class="answers"> <table> <tbody> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">A. 10%</div> </td> </tr> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">B. 15%</div> </td> </tr> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">C. 20%</div> </td> </tr> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">D. 25%</div> </td> </tr> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">E. 30%</div> </td> </tr> </tbody> </table> </div>"	| "<p><b>Arithmetic Percents</b></p><p>A reduction of $150 from $500 represents a percent decrease of        <img src="data:image/png;base64,iVBORw***SuQmCC "/>. Therefore, the price of the coat was reduced by 30%.    </p>"	|"e"	|"Quant Problem Solving"	|"NULL"|	"NULL"|	"NULL"	|"NULL"|

\*** => truncated data

[SAMPLE RENDERED HTML](https://codepen.io/utkarshmttl/full/vdKZwo/)