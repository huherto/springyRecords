---
layout: page
title: Getting started
---

It is very easy to get started and create your own customized code generator.

Download, build and install the springRecords jar in your local maven repository.

{% highlight bash %}

git clone https://github.com/huherto/springyRecords.git

cd springyRecords/generator

mvn install

cd ../..

{% endhighlight %}

Make a copy of example-generator to start your project.

{% highlight bash %}

cp -r springyRecords/example-generator  ~/workspace/yourdir

{% endhighlight %}

Add database driver dependencies in the pom.xml <a href="{{ site.github.repo}}/tree/master/example-generator/pom.xml">"pom.xml</a>

{% highlight bash %}

cd ~/workspace/yourdir

vim pom.xml 

{% endhighlight %}

Create datasource and customize code generation. 

<a href="{{ site.github.repo}}/tree/master/example-generator/src/main/java/com/example/Application.java">Application.java</a>

{% highlight bash %}

vim src/main/java/com/example/Application.java

{% endhighlight %}

Build and execute using spring-boot.

{% highlight bash %}

maven package

java -jar target/example-generator-0.1.jar 

{% endhighlight %}

That is all. You can now check your project.
