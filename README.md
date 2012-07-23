<html>
<body>
<p>
Library of 10000 books<br/>
Built with 
<ul>
<li><a href="https://developers.google.com/web-toolkit/">GWT</a>, </li>
<li><a href="http://gwtbootstrap.github.com/">GWTBootstrap</a>, </li>
<li><a href="https://developers.google.com/appengine/docs/java/search/overview">App Engine Search API</a>, </li>
<li><a href="http://code.google.com/p/objectify-appengine/">Objectify</a></li>
<li>and other App Engine features such as CRON, Mail, Authentication, ...</li>
</ul>
<br/>
</p>
<p>
Notes about the restricted use of the App Engine platform:
<ol>
<li>
<p>The Search API returns only 1000 items, 
<a href="https://developers.google.com/appengine/docs/java/search/overview#Quotas">&#8594; Maximum Search Offset: 1000</a>
 <br/>
So, it can NOT be used to make a proper search directly on 10000 books... Let's use it for suggestions only.
</p>
</li>
<li>
<p>
Then, Objectify is used to query the books. <br/>
But, for an advanced pagination such as with the google search pages ([1..10]), we would use limit+offset.<br/>
However, this is far too much <a href="https://developers.google.com/appengine/articles/paging">resource-consuming</a> when you look for thousands of records (also with 2000 books, I could see how the quota "datastore small operations" reached its maximum after only some browsing).<br/>
So, the alternative is to use a <a href="https://developers.google.com/appengine/docs/java/datastore/queries#Query_Cursors">cursor</a>. But, it gives you only the next or previous link (so, you can not jump from page 1 to page 10).
<br/>Also, the cursor has the limitation of not being possible with "IN" queries, cf "Limitations of Cursors". It means that the feature of queries like "author A <i>OR</i> author B" can not be used anymore.
</p>
</li>

</ol>


<a href="http://pgu-books.appspot.com/"><strong>&#8594; Go to the website</strong></a><br/>
<br/>
</p>
<p>
TODOs<br/>
<ul>
<li>add more books from .txt</li>
<li>include books search engines</li>
</ul>
</p>
</body>
</html>
