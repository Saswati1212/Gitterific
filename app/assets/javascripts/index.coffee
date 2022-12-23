$ ->
  # Requests a web socket from the server for two-way fully duplex communication
  ws = new WebSocket $("#gitterific-home").data("ws-url")
  # On receiving a message, checks the response type and renders data accordingly
  ws.onmessage = (event) ->
    message = JSON.parse event.data
    switch message.responseType
      when "searchResults"
        $("#search-page").show()
        ComposeSearchPageHtml(message)
        $("#search-page-results").css("display", "flex")
        $("#repository-details").hide()
        $("#topic-page-result").hide()
        $("#user-details-result").hide()
        $("#search-page-results").show()
      when "userDetails"
        $("#search-page-results").hide()
        $("#user-details-result").show()
        $("#search-page").hide()
        ComposeUserPageHtml(message)
        $("#repository-details").hide()
        $("#topic-page-result").hide()
      when "topicsDetails"
        $("#topic-page-result").show()
        ComposeTopicSearchHtml(message)
        $("#search-page").hide()
        $("#search-page-results").hide()
        $("#repository-details").hide()
        $("#user-details-result").hide()
      when "repoProfile"
        $("#search-page").hide()
        $("#search-page-results").hide()
        $("#topic-page-result").hide()
        $("#topic-page-result").hide()
        $("#user-details-result").hide()
        $("#repository-details").css("display", "flex")
        ComposeRepoProfileHtml(message)
        $("#repository-details").show()


  $("#form").submit (event) ->
      event.preventDefault()
      phrase = $("#search").val()
      console.log(phrase)
      if phrase == ""
        alert "search cant be empty"
        return false
      else
        ws.send(JSON.stringify({searchPage: phrase}))
        $("#search").val("")
        return

  $("#search-page-results").on "click", "a.user-details-link", (event) ->
    event.preventDefault()
    ws.send(JSON.stringify({userDetails: $(this).text()}))
    return
   $("#search-page-results").on "click", "a.topic-link", (event) ->
    event.preventDefault()
    ws.send(JSON.stringify({topicsDetails: $(this).text()}))
    return
  $("#search-page-results").on "click", "a.repo-details-link", (event) ->
    event.preventDefault()
    ws.send(JSON.stringify({repoProfile: $(this).text(), username: $(this).attr("username")}))
    return
   $("#topic-page-result").on "click", "a.user-details-link", (event) ->
    event.preventDefault()
    ws.send(JSON.stringify({userDetails: $(this).text()}))
    return
   $("#topic-page-result").on "click", "a.topic-link", (event) ->
    event.preventDefault()
    ws.send(JSON.stringify({topicsDetails: $(this).text()}))
    return
   $("#topic-page-result").on "click", "a.repository-details", (event) ->
    event.preventDefault()
    ws.send(JSON.stringify({repositoryDetails: $(this).text()}))
    return
   $("#user-details-result").on "click", "a.repo-details-link", (event) ->
    event.preventDefault()
    ws.send(JSON.stringify({repoDetails: $(this).text()}))
    return

ComposeSearchPageHtml =  (message) ->
  #console.log("heys")
  #console.log(message)
  $("#search-page-results").empty()
  $("#repoTable tbody tr").remove()
  searchTable = $("<table>").prop("class", "table").prop("border","1")
  searchTable.append "<thead><tr><th>Repository</th><th>User</th><th>Topics</th></thead><tbody>"
  for key,val of message.searchMap
    for repository in val
        searchData = $("<tr>")
        repositoryLink = $("<a>").text(repository.repoName).attr("class", "repo-details-link").attr("username",repository.userName)
        repository_user = $("<td>").append(repositoryLink).append("</td>")
        searchData.append(repository_user)
        userProfileLink = $("<a>").text(repository.userName).attr("class", "user-details-link")
        userData  = $("<td>").append(userProfileLink).append("</td>")
        searchData.append(userProfileLink)
        topicList =$("<p>").text("")
        for topic in repository.topics
            topicLink = $("<a>").text(topic).attr("class","topic-link")
            topicList.append(topicLink)
        topicData = $("<td>").append(topicList).append("</td>")
        searchData.append(topicData)
        searchData.append($("</tr>"))
        searchTable.append(searchData)
    searchTable.append($("</tbody>"))
    $("#search-page-results").append(searchTable)
    
#   $("#search-page-results").append($("<table>"))

#   #$("#search-page-results").append($("<h1>").text("dsfsf"))
#   console.log(message.searchMap)
#   for key,val of message.searchMap
#     console.log(val)
#     #$("#search-page-results").append($("<h3>").text("Query:" + key))
#     for repository in val
#             userLink = $("<a>").text(repository.userName).attr("class", "user-details-link")
#             user = $("<p>").append(userLink).append("</p>")
#             $("#search-page-results").append(user)
#             #$("#search-page-results").append('<tr><td>'+repository.userName+'</td><td>'+repository.repoName+'</td><td>'+repository.topics+'</td></tr>')
#             repositoryLink = $("<a>").text(repository.repoName).attr("class", "repo-details-link")
#             repo = $("<p>").append(repositoryLink).append("</p>")
#             $("#search-page-results").append(repo)
#             $("#search-page-results").append($("<p>").text("topics:"))
#             for topic in repository.topics
#                 topicLink =  $("<a>").text(topic).attr("class", "topic-details-link")
#                 #topicData =  $("<p>").append(topicLink).append("</p>")
#                 $("#search-page-results").append(topicLink)
#                 $("#search-page-results").append()
#    ComposeSearchPageHtml =  (message) ->
#      $("#search-page-results").empty()
#      keys = message.searchMap.keys
#      for key,value of message.searchMap
#        if(typeof value == "object")
#            $('#search-page-results').append "<br/><br/><b>" + "Search term :  " +key + "<br/><br/>"
#            searchTable = $("<table>").prop("class", "table").prop("border","1")
#            searchTable.append "<thead><tr><th>User</th><th>Repository</th><th>Topics</th></thead><tbody>"
#            getSearchDetails value, searchTable
#            $("#search-page-results").append(searchTable)


#    getSearchDetails = (objectValue, searchTable ) ->
#            for key,value of objectValue
#                searchData = $("<tr>")
#                if(typeof value == "object")
#                    getSearchRepoValues value , searchData
#                searchTable.append(searchData)


#    getSearchRepoValues = (objectValue, searchData ) ->
#            for key,value of objectValue
#                if(key=="owner")
#                    userLink = $("<a>").text(value).attr("class", "user-details")
#                    owner = $("<td>").append(userLink).append("</td>")
#                else if(key=="name")
#                    repositoryLink =  $("<a>").text(value).attr("class", "repository-details").attr("username",objectValue['owner'])
#                    repository = $("<td>").append(repositoryLink).append("</td>")
#                else if(key=="topics")
#                    topicsData =  $("<td>")
#                    for element,val of value
#                        topicLink =  $("<a>").text(val).attr("class","topic-link")
#                        topicsData.append(topicLink).append("</td>")
#            searchData.append(owner).append(repository).append(topicsData).append("</tr>")

#     searchTable = $("<table>").prop("class", "table").prop("border","1")
#     searchTable.append "<thead><tr><th>Repository</th><th>User</th><th>Topics</th></thead><tbody>"
#     for repository in message.searchProfile.searchProfile.repos
#         searchData = $("<tr>")
#         repositoryLink = $("<a>").text(repository.name).attr("class", "repository-details").attr("username",repository.owner)
#         repository_user = $("<td>").append(repositoryLink).append("</td>")
#         searchData.append(repository_user)
#         userProfileLink = $("<a>").text(repository.owner).attr("class", "user-details")
#         userData  = $("<td>").append(userProfileLink).append("</td>")
#         searchData.append(userProfileLink)
#         topicList =$("<p>").text("")
#         for topic in repository.topics
#             topicLink = $("<a>").text(topic).attr("href", "/getReposByTopics/"+topic).attr("class","topic-link")
#             topicList.append(topicLink)
#         topicData = $("<td>").append(topicList).append("</td>")
#         searchData.append(topicData)
#         searchData.append($("</tr>"))
#         searchTable.append(searchData)
#     searchTable.append($("</tbody>"))
#     $("#topic-page-result").append(searchTable)


ComposeUserPageHtml =  (message) ->
  #console.log("heys")
  #console.log(message)

  $("#user-details-result").empty()
  userData = $("<p>").text("user")
  profile = message.searchProfile
  userData.append($("<br>"))
  userData.append("Login Name:"+ profile.login).append("<br>")
  userData.append("Name:" + profile.name).append("<br>")
  userData.append("UserId:" + profile.repoId)
  userData.append($("<ol>"))
  for repository in profile.repositories
    repositoryLink = $("<a>").text(repository.name).attr("class", "repo-details-link").append("</a>")
    repo = $("<li>").append(repositoryLink).append($("<p>").text(":" + repository.description)).append("</li>")
    userData.append(repo)
  $("#user-details-result").append(userData)

ComposeTopicSearchHtml = (message) ->
    console.log("heys")
    console.log(message)
    $("#topic-page-result").empty()
    topicName = message.keyword
    $("#topic-page-result").append($("<h3>").text("Repository from topic : "+ topicName))
    searchTable = $("<table>").prop("class", "table").prop("border","1")
    searchTable.append "<thead><tr><th>User</th><th>Repository</th><th>Topics</th></thead><tbody>"
    for repository in message.searchProfile
        searchData = $("<tr>")
        userProfileLink = $("<a>").text(repository.user).attr("class", "user-details-link")
        userData  = $("<td>").append(userProfileLink).append("</td>")
        searchData.append(userProfileLink)
        repositoryLink = $("<a>").text(repository.repo).attr("class", "repository-details")
        repository_user = $("<td>").append(repositoryLink).append("</td>")
        searchData.append(repository_user)
        topicList =$("<p>").text("")
        for topic in repository.topics
            topicLink = $("<a>").text(topic).attr("class","topic-link")
            topicList.append(topicLink)
        topicData = $("<td>").append(topicList).append("</td>")
        searchData.append(topicData)
        searchData.append($("</tr>"))
        searchTable.append(searchData)
    searchTable.append($("</tbody>"))
    $("#topic-page-result").append(searchTable)

ComposeRepoProfileHtml = (message) ->
  console.log(message)
  $("#repo-page-result").empty()
  repoTable = $("<table>").prop("class", "table").prop("border","1")
  repoTable.append "<tr>Repository</tr>"
  repoTable.append "<thead><tr><th>Committer</th><th>Commit Count</th><th>Min Add</th><th>Max Add</th><th>Avg Add</th><th>Min Del</th><th>Max Del</th><th>Avg Del</th></thead><tbody>"
  for repository in message.searchRepoMap
      searchData = $("<tr>")
      userProfileLink = $("<a>").text(repository.user).attr("class", "user-details")
      userData  = $("<td>").append(userProfileLink).append("</td>")
      searchData.append(userProfileLink)
      searchData.append($("<td>").text(repository.commit_count).append("</td>"))
      searchData.append($("<td>").text(repository.min).append("</td>"))
      searchData.append($("<td>").text(repository.max).append("</td>"))
      searchData.append($("<td>").text(repository.avg).append("</td>"))
      searchData.append($("<td>").text(repository.delMin).append("</td>"))
      searchData.append($("<td>").text(repository.delMax).append("</td>"))
      searchData.append($("<td>").text(repository.delAvg).append("</td>"))
      searchData.append($("</tr>"))
      repoTable.append(searchData)
    repoTable.append($("</tbody>"))
    $("#repo-page-result").append(repoTable)