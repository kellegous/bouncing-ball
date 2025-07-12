# Bouncing Ball

In 2006, I wrote a silly bouncing ball simulation in Java as the week 1 assignment
in Neil Gershenfeld's Nature of Mathematical Modeling course at the MIT Media Lab. I later put the Java Applet in a [blog post](https://kellegous.com/j/2006/02/19/bouncing-ball/) on my site. Java Applets are a thing of the past and to this day I still see in Fullstory a number of people who find their way to the page and then click repeatedly on the Applet expecting to see the ball bounce. From time to time, I think I should recreate the simulation using modern web technologies but I never get around to it. Today, though, I decided to see if this was a take that was suitable for an LLM agent.

I gave cursor the following prompt,

> In 2006, I wrote a Java Applet that simulated a bouncing ball using a simple model that simulated angular velocity changes on collisions. The code for that applet is in the `@/java` directory. I want to recreate that simulation in an HTML canvas in the vite app. The entry point for the vite app is `@index.html` . Can you port the Java into Typescript?

I had already created the vite app using `npm create vite@latest bouncing-ball -- -template vanilla-ts` and removed most of the placeholder code. What is in this repository is what resulted. It's a pretty good start.
