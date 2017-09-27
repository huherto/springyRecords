---
layout: post
title: Printing a query
---

Using *io.github.huherto.springyRecords.PrintRowCallbackHandler*, is very easy to print the results of a query. This is very useful while you are writing code, or debugging.

{% highlight java %}

    JdbcTemplate jt = new JdbcTemplate(createDs());

    String sql = "select * from pet ";
    jt.query(sql, new PrintRowCallbackHandler());

{% endhighlight %}

This goes into the standard output by default (System.out).

{% highlight java %}
name=Manchas, owner=Humberto, species=Dog, sex=M, birth=2014-03-23, death=null
{% endhighlight %}

## Printing Columns

{% highlight java %}

    JdbcTemplate jt = new JdbcTemplate(createDs());
    String sql = "select * from pet ";
    String format = "%-10s %-10s %-10s %-10s %-10s %-10s";
    System.out.println(String.format(format, "name", "owner" , "species", "sex", "birth" ,"death"));
    jt.query(sql, new PrintRowCallbackHandler(format));

{% endhighlight %}

Output...

{% highlight java %}

name       owner      species    sex        birth      death     
Manchas    Humberto   Dog        M          2014-03-23 null  

{% endhighlight %}

