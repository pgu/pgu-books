<html>
<body>
<p>
Library of 10000 books<br/>
GWT, gwtbootstrap, app engine API search<br/>
http://gwtbootstrap.github.com/<br/><br/>
<ol>
<li>
<p>Search API returns only 1000 items, 
<a href="https://developers.google.com/appengine/docs/java/search/overview#Quotas">see here</a>
 (Maximum Search Offset: 1000)<br/>
So, it can NOT be used to make a proper search directly on books. I use it only for suggestions.
</p>
</li>
<li>
<p>
Then, Objectify is used to query the books. <br/>
But, for an advanced pagination such as the google search page ([1..10]), we would use limit+offset.<br/>
However, this is too resource-consuming when you look for thousands of records (also quoted <a href="https://developers.google.com/appengine/articles/paging">here</a>).<br/>
So, the alternative is using a cursor (<a href="https://developers.google.com/appengine/docs/java/datastore/queries#Query_Cursors">here</a>). But, it gives you only the next or previous link (so, you can not jump from page 1 to page 10).
<br/>Also, the cursor has the limitation of not being possible with "IN" queries, cf "Limitations of Cursors"
</p>
</li>

</ol>


<a href="http://pgu-books.appspot.com/"><strong>&#8594; Go to the website</strong></a><br/>
<br/>
</p>
<p>
TODOs<br/>
=====<br/>
* add more books from .txt<br/>
* deal with requests with more than 1000 books<br/>
* include books search engines<br/>
* include charts<br/>
</p>
</body>
</html>
