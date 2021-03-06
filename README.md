# RecruitmentBuddy
This project is intended to help sorority members who must undergo training for Recruitment. Recruitment training often involves large amounts of important information that is distributed through a variety of media, such as emails, links to presentation slides, or date spreadsheets. The volume and variety of information makes it difficult to keep track of, and losing a piece of information can cause a sister to miss a meeting or deadline. Sisters who cannot find a resource often ask sorority officers or other members for assistance, but this method tends to be unreliable and places an additional burden on other members, especially officers.

  To facilitate the process of locating and accessing resources, this project will provide a simple text message interface that allows sisters to ask questions and request resources. The interface will included a series of commands for common questions, such as upcoming meeting dates, upcoming deadlines, and outfit requirements for a given event. After a user texts in a command, they will receive a text response with an answer to their question. Additional commands will be included to request resources, such as presentations and documents stored in Google Drive. These commands will send a link to the requested resource back the user. The question responses and resource links will initially be stored internally, but if time allows the system will be adapted to scrape the data from online resources so it can easily stay up-to-date. If the system does not have the answer to a question or request, it will pass the question along to a relevant officer via text message, and will send the officer's response back the original asker.
  
This project will benefit sorority sisters undergoing Recruitment training by allowing them to quickly access a variety of resources from a centralized endpoint. The text interface will also allow them to access the system on-the-go, without the need to use a laptop or desktop computer. This system will also reduce the burden placed on officers and other members, who are typically asked the same questions by many people, by automatically answering common questions and passing questions along to officers only is the answer is unknown.

# Questions:
1. Are you currently an active member of a sorority? If so, which one?
2. How much experience do you have with sorority Recruitment?
3. Is the Recruitment process enjoyable for you?
4. What training have you undergone to prepare for Recruitment?
5. What do you think is missing from the Recruitment training process?
6. What do you think the most valuable part of the training process is?
7. Do you ever feel overwhelmed by the amount of information distributed for Recruitment?
8. How do you keep track of requirements for Recruitment, such as dates and clothing?
9. How often do you feel the need to access Recruitment Resources (such as slides, dates, etc.)?
10. When you need a resource but don’t know where it’s located, how do you go about finding it?
11. Have you ever attempted to find such a resource, but were not able to find it?
12. What are the consequences for not being able to find a resource?

# Answers:

## Question 1:
1. I am a member of Alpha Delta Pi.
2. I am a member of Alpha Delta Pi.
3. I am a member of Alpha Delta Pi.

## Question 2:
1. I went through once as a PNM, and was on the recruiting side once. I have also attended chapter training sessions for Recruitment.
2. I’ve been through it one time as PNM and I’ve been on the other side for one year, so I’ve had two years of experience from different perspectives.
3. I went through it twice as a PNM, and once on the member side.

## Question 3:
1. Yes, it’s great but there’s ups and downs. It’s great to meet new people and bond with your sisters.
2. It is, it’s tiring but from the perspective of a sorority member, it was a great bonding experience and brought me closer to my sisters.
3. No, I did not like it. It’s a lot of small talk, and I don’t like small talk. 

## Question 4:
1. Our chapter has Recruitment Workshops to learn about the topics encountered during Recruitment. This year, I only have a few refresher workshops that are very interactive and involve practice.
2. In my first year as a sister, there was more intensive training (bimonthly meetings and training in January), and this year there has been minimal training that is mostly a refresher.
3. We have a lot of workshops, usually every two weeks or once a month. There are more workshops for younger members, who have meetings every week.

## Question 5:
1. I would love to have more practice opportunities with strangers for conversation, since practicing with sisters is not as realistic.
2. I think that realistic scenarios would help to practice things like conversations and transitions would be helpful.
3. I don’t think anything is missing.

## Question 6:
1. Having workshops that involve both learning with slides and examples, and being able to actually practice things.
2. I think that the interactive practice is helpful, as well as learning the logistic details of how the system works for recruiting and understanding the algorithm behind everything.
3. You get to feel more comfortable so the actual interactions during Recruitment can be more natural, and you can ask the right questions to get to know people better.

## Question 7:
1. I feel overwhelmed by the information distributed by the chapter in general, but not by Recruitment specifically.
2. Without the opportunity to see how it works, the first time I was exposed to the logistics was overwhelming but it made more sense when I saw it play out in real like. There are also terms that we use during Recruitment that are difficult to learn the first time, but are easy to understand after you learn them.
3. No, I think it’s manageable.

## Question 8:
1. I put dates in my calendar, and try to remember clothing throughout the semester.
2. I get my clothing requirements done, and for dates I make sure my calendar is up to date and I think ahead to plan my schedule. There’s a lot of help from leadership in reminding people of dates and responsibilities.
3. I use my phone for calendar events, and I don’t write the outfits down. I’ll look at the slides if I need to remember things about outfits.

## Question 9:
1. At this time of year, I access resources about once a week.
2. I usually see the information in weekly emails and use those as a reference.
3. I access them very little because I have a good a memory, and can remember the information on my own.

## Question 10:
1. I usually send a message in the chapter GroupMe.
2. I would reach out to one of the officers in charge of Recruitment or one of my peers in the sorority.
3. First, I would look at the weekly Chapter Announcements. Next, I usually ask my friends, some of whom are officers, or send a message to one of the chapter’s group chats.

## Question 11:
1. Over the summer, I was looking for a PowerPoint that had been sent but I couldn’t find the email it had been sent in.
2. No, I have not.
3. No, I always find them.

## Question 12:
1. People are pretty responsive, but if I couldn’t get a response I would miss opportunities to buy something (like clothing).
2. There is the potential for missing a meeting and getting a consequence such as a fine, and being a detriment to the chapter by missing an event.
3. I could be unprepared or could miss a meeting, which would result in a fine.

# Requirements
* Commands for questions about:
  * Recruitment dates and times
  * Recruitment Workshop dates and times
  * Format for sending in Recruitment excuses
  * Required clothing items for a given event
* Commands for requesting a link to:
  * Recruitment workshop slides
  * Spreadsheet of all Recruitment dates
  * Spreadsheet of excuse deadlines
* Sends question/request on to relevant officer if does not match a supported command, and sends the officer's response back to the asker

# Development Approach
In the first stage of my process, I have empathized with the users and collected requirements. As I am personally a sorority officer who is highly involved with Recruitment, I was able to draw upon my own experience as well as my interviews with three other sorority members. In my own experience, many members, especially new members, have trouble keeping track of different resources and requirements related to Recruitment, and reatedly ask similar questions to officers and other members. All interviewees listed reaching out to officers and/or other chapter members when asked how they go about finding resources, and as an officer, I can personally attest that it is overwhelming to be asked so many questions. Thus, the motivation of this systems is to automatically answer as many common recruitment questions as possible to reduce the burden on officers and other members who may receive questions.

After empathizing with the user and gathering requirements, I will brainstorm possible solutions. In my brainstorming, I will consider possible formats and structures for commands, possible methods of collecting and storing the information that will be used to answer questions, and how data should be updated. I will also consider how the system architecture could best support these features. In designing the system architecture and planning for development, I will focus on scalalbility and extendability. Although the initial focus of this application is Recruitment resources, if successful, it could also be applied to scholarship, philanthropy, or other types of sorority resources. The initial design will focus on resources for a single sorority chapter, but the sytem should be able to scale to support multiple chapters in the future.

To ensure that development is sustainable, I will initially implement functionality only for question commands. Once the question commands have been completed and are functional, I will move on the request commands, and then to functionality that asks officers for unknown ansers. By starting with a subset of the system and gradually building onto it, I will be able to test and evaluate my system at each stage before moving on to the next one. As in Agile development, this will allow me to fail and recover quickly, and should prevent me from going too far down an unsuccessful or unsustainable path. I will base as much of my new architecture as possible on the existing project architecture in order to further reduce risk.

To test the system, I will initially test all features individuallly using unit tests to ensure that the features' functionality is working correctly, and then will test the system's overall functionality. After I have personally tested the system, I will train a small group of sorority members in using the system and will ask them to use and evaluate its functionality. This will allow me to perform a more realistic tests, as sorority members who are unfamiliar with the development of the system will serve as its main users. If the pilot testing is successful, I will then release the system to the entire sorority chapter, and will train them in using the provided set of commands. After the system has been relesaed, I will maintain it by both updating the data it draws from, and addressing any bugs that come. I will also need to keep track of changes in the system's dependencies, such as updated packages, that could break the system and perform my own updates as needed. Since I will be graduating after next year, it will be important that I carefuly document the system so that future sorority members can use and maintain it when I am no longer able to.

In summary, this process will allow me to maintain focus on user requirements throughout development, while also reducing the risk of failure or unsustainable development.
