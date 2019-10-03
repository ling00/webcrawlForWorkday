# webcrawlForWorkday
a web crawler to crawl the job postings from workday(Refered from the python version on github)

To use the crawler:
Put the name and the url of the company in workday.json
eg 
{
  "companyName": "mastercard",
  "companyUrl": "https://mastercard.wd1.myworkdayjobs.com/CorporateCareers"
}
Result:
All the job postings for this company will be saved as json file in the data folder

How it works:
1.Get the data from the Workday api
2.Get the pagenation of the url
3.Get the max number of page it can crawl
4.Crawl the job using multithread 

