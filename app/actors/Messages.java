package actors;

import com.fasterxml.jackson.databind.JsonNode;

public class Messages {
    public static final class TrackSearch {
        public final String searchQuery;
        public final String requestType;

        /**
         * @param searchQuery Search query to be tracked by <code>SearchActor</code>
         * @param requestType Indicates whether the request is a periodic search query sent by the search actor itself or a request sent from client side
         */
        public TrackSearch(String searchQuery, String requestType) {
            this.searchQuery = searchQuery;
            this.requestType = requestType;
        }
    }

    public static final class GetUserDetails {

        public final String username;

        public GetUserDetails(String username) {
            this.username = username;

        }
    }

    public static final class UserDetails {
        public final JsonNode userDetails;

        public UserDetails(JsonNode userDetails) {
            this.userDetails = userDetails;
        }
    }


    public static final class SearchPageActor{
        public final String phrase;

        public SearchPageActor(String phrase) {
            this.phrase = phrase;
        }
    }

    public static final class SearchResult {
        public final JsonNode searchResult;
        public SearchResult(JsonNode searchResult) {
            this.searchResult = searchResult;
        }
    }


   public static final class GetRepositoryfromTopic {
       public final String topicName;

       public GetRepositoryfromTopic(String topicName) {
           this.topicName = topicName;
       }
   }

   public static final class TopicDetails {
       public final JsonNode topicDetails;

       public TopicDetails(JsonNode topicSearchResult) {
           this.topicDetails = topicSearchResult;
       }
   }

   public static final class RepoProfile {
       public final JsonNode repopro;
       public RepoProfile(JsonNode repopro) {
           this.repopro = repopro;
       }
   }

   public static final class GetRepoName {
       public final String reponame;
       public GetRepoName(String reponame) {
           this.reponame = reponame;
       }
   }



}
