Library of 8000 books
---

Built with:

- [GWT] [1]
- [GWTBootstrap] [2]
- App Engine APIs:
 - [Search] [3]
 - [Cron] [5]
 - [Mail] [6]
 - [Logs] [7]
 - [Datastore indexes] [8]
- [Objectify] [4]

And monitored with [Appstats] [13].

Website
---
[<strong>&#8594; Go to the website</strong>] [12]

Notes
---
About the restricted use of the App Engine platform:

The Search API returns only 1000 items, [&#8594; Maximum Search Offset: 1000] [9]. 
So, it can NOT be used to make a proper search directly on 8000 books... Let's use it for suggestions only.

Then, Objectify is used to query the books.
But, for an advanced pagination such as with the google search pages ([1..10]), we would use limit+offset.

However, this is far too much [resource-consuming] [10] when you look for thousands of records 
(with 2000 books, I could see how the quota "datastore small operations" reached its maximum after only some browsing).

So, the alternative is to use a [cursor] [11]. 
But, it gives you only the next or previous link (so, you can not jump from page 1 to page 10).


Also, the cursor has the limitation of not being possible with "IN" queries, cf "Limitations of Cursors". 
It means that the feature of queries like "author A <i>OR</i> author B" can not be used anymore.


[1]: https://developers.google.com/web-toolkit/                           "GWT"
[2]: http://gwtbootstrap.github.com/                                      "GWTBootstrap"
[3]: https://developers.google.com/appengine/docs/java/search/overview    "App Engine Search API"
[4]: http://code.google.com/p/objectify-appengine/                        "Objectify"
[5]: https://developers.google.com/appengine/docs/java/config/cron        "Cron"
[6]: https://developers.google.com/appengine/docs/java/mail/overview      "Mail"
[7]: https://developers.google.com/appengine/docs/java/logs/overview      "Logs"
[8]: https://developers.google.com/appengine/docs/java/datastore/indexes  "Datastore indexes"

[9]: https://developers.google.com/appengine/docs/java/search/overview#Quotas "Quotas"
[10]: https://developers.google.com/appengine/articles/paging "Paging"
[11]: https://developers.google.com/appengine/docs/java/datastore/queries#Query_Cursors "Cursor"
[12]: http://pgu-books.appspot.com/ "pgu-books"

[13]: https://developers.google.com/appengine/docs/java/tools/appstats "Appstats"
