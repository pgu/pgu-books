Website
---
[**&#8594; Go to the website**] [pgu_books]

Library of 8000 books
---

Built with:

- [GWT]
- [GWTBootstrap]
- App Engine APIs:
 - [Search]
 - [Cron]
 - [Mail]
 - [Logs]
 - [Datastore indexes] [datastore]
- [Objectify]

And monitored with [Appstats].

Notes
---
About the restricted use of the App Engine platform:

The Search API returns only 1000 items, [&#8594; Maximum Search Offset: 1000] [max_offset]. 
So, it can NOT be used to make a proper search directly on 8000 books... Let's use it for suggestions only.

Then, Objectify is used to query the books.
But, for an advanced pagination such as with the google search pages ([1..10]), we would use limit+offset.

However, this is far too much [resource-consuming] [consom] when you look for thousands of records 
(with 2000 books, I could see how the quota "datastore small operations" reached its maximum after only some browsing).

So, the alternative is to use a [cursor]. 
But, it gives you only the next or previous link (so, you can not jump from page 1 to page 10).


Also, the cursor has the limitation of not being possible with "IN" queries, cf "Limitations of Cursors". 
It means that the feature of queries like "author A *OR* author B" can not be used anymore.

[GWT]: https://developers.google.com/web-toolkit/                           "GWT"
[GWTBootstrap]: http://gwtbootstrap.github.com/                             "GWTBootstrap"
[Search]: https://developers.google.com/appengine/docs/java/search/overview "App Engine Search API"
[Objectify]: http://code.google.com/p/objectify-appengine/                  "Objectify"
[Cron]: https://developers.google.com/appengine/docs/java/config/cron       "Cron"
[Mail]: https://developers.google.com/appengine/docs/java/mail/overview     "Mail"
[Logs]: https://developers.google.com/appengine/docs/java/logs/overview     "Logs"
[datastore]: https://developers.google.com/appengine/docs/java/datastore/indexes  "Datastore indexes"

[max_offset]: https://developers.google.com/appengine/docs/java/search/overview#Quotas "Quotas"
[consom]: https://developers.google.com/appengine/articles/paging "Paging"
[cursor]: https://developers.google.com/appengine/docs/java/datastore/queries#Query_Cursors "Cursor"
[pgu_books]: http://pgu-books.appspot.com/ "pgu-books"

[Appstats]: https://developers.google.com/appengine/docs/java/tools/appstats "Appstats"
