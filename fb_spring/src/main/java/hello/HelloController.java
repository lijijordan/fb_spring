package hello;

import javax.inject.Inject;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FriendList;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {

    private Facebook facebook;
	private ConnectionRepository connectionRepository;

    @Inject
    public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
		this.connectionRepository = connectionRepository;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }

        model.addAttribute("facebookProfile", facebook.userOperations().getUserProfile());
        PagedList<Post> feed = facebook.feedOperations().getFeed();
        facebook.friendOperations().getFriendProfiles();
        model.addAttribute("feed", feed);
        
        PagedList<FriendList> friends = facebook.friendOperations().getFriendLists();
        for (FriendList f : friends) {
        	System.out.println("id:" + f.getId());
        	System.out.println("name:" + f.getName());
        	System.out.println("extra data:" + f.getExtraData());
        	System.out.println("list type:" + f.getListType());
		}
        model.addAttribute("friends", friends);
        
        return "hello";
    }

}
