# webcrawlForWorkday
<p>This is a web crawler that crawls the job postings from workday (The alogorithm is refered from the python version on github)

<p>To use the crawler:
<p>Put the name and the url of the company in workday.json

<p>eg 
<p>{
 "companyName": "mastercard",
 <p> "companyUrl": "https://mastercard.wd1.myworkdayjobs.com/CorporateCareers"
}
<p>
Result:
<p>All the job postings for this company will be saved as json file in the data folder
<p>
How it works:
<p>1.Get the data from the Workday api
<p>2.Get the pagenation of the url
<p>3.Get the max number of page it can crawl
<p>4.Crawl the job postings with thread 

