Question: There is an API that one must call to get data. The trouble is it will not let you cross the
limit of call - say 15 calls per minute. If you cross the limit, the system penalizes you by one
additional minute of penalty where you can not make any call. Here is how the API looks
like: function string call_me(string input).
Propose a solution by which:
1. You would be able to use the API within the safe limit.
2. What happens if you are supposed to call the API 20 times per minute? Is there
   any way to accomplish this?
3. If you were the API designer, what would you do to implement this behavior?

Answers:
1. We can use caching techniques to keep api request within safe limit. (checkout to commit #2)
2. & 3. We can implement pricing-plan (free, basic, premium) so that, consumer can hit the api more that free available rate limit.


There's one backend Spring Boot based RESTApi web service (SpringBootApp.java) which has to be started in order to test the solution. 
And then there's a client (ApiConsumerApp.java) which should be run as a separate process, it will test all the api calls scenarios.