<img src="https://user-images.githubusercontent.com/20863182/36215111-2f9418fc-11d1-11e8-9375-9f3af5bd9954.png" align="right" height="200" width="200"/>

# eModules

An android application that enables the user to practice from a set of questions, flag them and analyse his performance.

<br>

>Demo of the app can be seen [here](http://kungfucat.me/images/portfolio/full/emodules.mp4).

<br><br>

## Screenshots

<img src="https://user-images.githubusercontent.com/20863182/36216899-11f163a4-11d6-11e8-9561-72ea87316e42.png" >

<br>

## Features

- Completely offline
- Progress Tracking
- Group Searches on the basis of :
  - Topics
  - Flagged
  - Incorrect
  - Correct
  - Unattempted
  - Keyword
- Doubts section (For viewing previously flagged questions with marked response and explanations)
- Timer (starts automatically and stops on submit button-press)
- Analytics
- Notepad in each question 

<br>

## Mockups

<img src="https://raw.githubusercontent.com/utkarshmttl/eModules/master/Mockups/Mockup1.png"/> <img src="https://raw.githubusercontent.com/utkarshmttl/eModules/master/Mockups/Mockup2.png"/> 
<br><br>

## Database Details
> Table name is ```questions```.

### Schema

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

>\* => TEXT is in HTML format and includes images in base64 format. So used webview to render this html content.

### Sample row
|ID|query|solution|correct|topic|notes|marked|time_txt|flagged|
|-|-|-|-|-|-|-|-|-|
"1"|	"<p><b>Book Question: 1</b></p><p>The price of a coat in a certain store is $500. If the price of the coat is to be       reduced by $150, by what percent is the price to be reduced?    </p><div class="answers"> <table> <tbody> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">A. 10%</div> </td> </tr> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">B. 15%</div> </td> </tr> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">C. 20%</div> </td> </tr> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">D. 25%</div> </td> </tr> <tr> <td> <div class="answercheck"></div> </td> <td> <div class="answer">E. 30%</div> </td> </tr> </tbody> </table> </div>"	| "<p><b>Arithmetic Percents</b></p><p>A reduction of $150 from $500 represents a percent decrease of        <img src="data:image/png;base64,iVBORw***SuQmCC "/>. Therefore, the price of the coat was reduced by 30%.    </p>"	|"e"	|"Quant Problem Solving"	|"NULL"|	"NULL"|	"NULL"	|"NULL"|


Sample rendered HTML can be found [here](https://codepen.io/utkarshmttl/full/vdKZwo/).

>This db is placed in assets/databases and [android-sqlite-asset-helper](https://github.com/jgilfelt/android-sqlite-asset-helper) library is used for shipping the app with pre-populated database. 

<br>

## Contributing

> To get started...

### Step 1

- 🍴 Fork this repo!

### Step 2

- 👯 Clone this repo to your local machine.

### Step 3

- **HACK AWAY!** 🔨🔨🔨

### Step 4

- 🔃 Create a new pull request using <a href="https://github.com/utkarshmttl/eModules/compare" target="_blank">`https://github.com/utkarshmttl/eModules/compare`</a>.

<br>

## Bug or Feature?
* If you find a  bug or want a new feature, open an [issue](https://github.com/utkarshmttl/eModules/issues).

<br>

## Authors

<a href="http://ducic.ac.in/"><img src="https://user-images.githubusercontent.com/16596327/30467922-9d4985ce-9a05-11e7-81aa-9f5348eb40de.png" align="right" width="300"/></a>


* **[Utkarsh Mittal](https://github.com/utkarshmttl)** - *utkarshmttl@gmail.com*

* #### Primary Developer : Harsh Bhardwaj ([kungfucat](https://github.com/kungfucat))

<br>

>This app was developed as a part of the [CMoC](https://github.com/clusterinnovationcentre/CMoC) program, mentored by [Utkarsh Mittal](https://github.com/utkarshmttl) and primarily developed by [Harsh Bhardwaj](https://github.com/kungfucat).

See also the list of [contributors](https://github.com/utkarshmttl/eModules/graphs/contributors) who participated in this project.
