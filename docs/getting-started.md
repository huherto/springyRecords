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

Make a copy of this directory to start your project.

{% highlight bash %}

cp -r springyRecords/example-generator  ~/workspace/my-generator

cd ~/workspace/my-generator

{% endhighlight %}

Add database driver dependencies in the pom.xml <a href="{{ site.github.repo}}/tree/master/example-generator/pom.xml">pom.xml</a>

{% highlight bash %}

vim pom.xml 

{% endhighlight %}

Create datasource and customize code generation modifing this file <a href="{{ site.github.repo}}/tree/master/example-generator/src/main/java/com/example/generator/Application.java">Application.java</a>

{% highlight bash %}

vim src/main/java/com/example/Application.java

{% endhighlight %}

Build and execute using spring-boot.

{% highlight bash %}

maven package

java -jar target/example-generator-0.1.jar 

{% endhighlight %}

That is all, checkout your project.
