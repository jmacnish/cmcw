Can't Miss, Can't Watch
=======================

Introduction
------------

A simple facebook application for finding out what new movies are available to rent on Netflix.
Try it out on Facebook: <http://apps.facebook.com/cantmisscantwatch/>

Details
-------

Technologies:

-   Grails 1.3.6
-   Tomcat 6.0.28
-   MySQL 5.1.49
-   c3p0 0.9.1.2 (for caching)
-   httpcore 4.0.1 / httpclient 4.0.1 (for fetching from remote APIs)

Integrates with the Netflix API for:
-   Movie catalog
-   Details about each movie

Development
-----------

Prerequisites:

-   JDK 1.6
-   Tomcat

Given the baseline schema, install it:

    mysql -uroot -p{pass} -dcmcw_dev <migrate.sql

Future
------

-   Save your selection of "watched"/"missed" videos
-   Allow others to view your complete selection of "watched"/"missed" videos (through the URL shared by
    posting)
-   Allow others to see your selection by browsing Facebook friends, while in the app itself.
-   Integrate with Netflix queue for automatically adding "watched" movies to your queue.
-   Allow dynamic scrolling for movie selection (adjusting the time period, and not just -4 days ago to +2 days
    from now)



