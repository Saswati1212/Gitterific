//package services;
//
//import com.typesafe.config.Config;
//import models.*;
//import services.*;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import play.Application;
//import play.inject.guice.GuiceApplicationBuilder;
//import play.mvc.Http;
//import play.test.WithApplication;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//import java.util.concurrent.CompletionStage;
//import java.util.concurrent.ExecutionException;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.Mockito.*;
//
///**
// * Test class for GithubService using mockito
// */
//@RunWith(MockitoJUnitRunner.Silent.class)
//
//public class GitServiceTest {
//
//
//    @Override
//    protected Application provideApplication() {
//        return new GuiceApplicationBuilder().build();
//    }
//
//    @InjectMocks
//    UserProfile userService;
//
//
//
//    /**
//     * tests the service getAllIssues
//     * @author Saswati Chowdhury
//     * @throws IOException
//     * @throws InterruptedException
//     * @throws ExecutionException
//     */
//    @Test
//    public void getUserDetailsTest() throws IOException, ExecutionException, InterruptedException {
//        when(userService.getUserProfile(any(String.class))).thenReturn((CompletionStage<User>) user());
//        CompletionStage<User> userDetails = userService.getUserProfile("userName");
//        assertNotNull(userDetails);
//       // UserProfile userDetails1 = userDetails.toCompletableFuture().get();
//        //assertEquals(userDetails.getName(),"MockUserName");
//    }
//
//
//
//
//    private User user()
//    {
//        User user = new User();
//        user.setName("MockUserName");
//        return user;
//    }
//
//}
