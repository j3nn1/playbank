package fi.jenniriikonen.playbank.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import fi.jenniriikonen.playbank.domain.CategoryRepository;
import fi.jenniriikonen.playbank.domain.Play;
import fi.jenniriikonen.playbank.domain.PlayRepository;


@Controller

public class PlayController {
	
	//Reporistories will be injected to controller
	@Autowired
	private PlayRepository prepository;
	
	@Autowired
	private CategoryRepository crepository;
	
	// Login page
    @RequestMapping(value="/login")
    public String login() {	
        return "login";
    }	
    

	//url playlist will launch this
		@RequestMapping("/playlist") 
		public String playList(Model model) {
			model.addAttribute("plays", prepository.findAll());
			return "playlist";
		}
	
	// Add play
	    @RequestMapping(value = "/add")
	    public String addPlay(Model model){
	    	model.addAttribute("play", new Play());
	    	model.addAttribute("categories", crepository.findAll());
	        return "addplay";
	    } 
	    
	    
	  // Save with validation 
	    //NOTE Validation bug: category field is empty after errors
	
	    @RequestMapping(value = "/save", method = RequestMethod.POST)
	    public String save(@Valid Play play, BindingResult bindingResult, Model model){
			if (bindingResult.hasErrors()) {
				//FIX: 
				model.addAttribute("categories", crepository.findAll());
				return "addplay";
			}
			
			model.addAttribute("play", play);
	    	prepository.save(play);
	        return "redirect:playlist";
	    }  
	  
	  // Delete play
    	//This will block url delete by users
	    @PreAuthorize("hasAuthority('ADMIN')")
	    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	    public String deletePlay(@PathVariable("id") Long playId, Model model) {
	    	prepository.deleteById(playId);
	        return "redirect:/playlist";
	    }   
	    
	 // Edit play
	    @RequestMapping(value = "/edit/{id}")
	    public String editBook(@PathVariable("id") Long playId, Model model){
	    	model.addAttribute("play", prepository.findById(playId));
	    	model.addAttribute("categories", crepository.findAll());
	    	return "editplay";
	    }
	    
	  // REST
	    // RESTful service to get all plays
	    @RequestMapping(value="/restplays", method = RequestMethod.GET)
	    public @ResponseBody List<Play> PlayListRest() {	
	        return (List<Play>) prepository.findAll();
	    }    

		// RESTful service to get play by id
	    @RequestMapping(value="/restplay/{id}", method = RequestMethod.GET)
	    public @ResponseBody Optional<Play> findPlayRest(@PathVariable("id") Long playid) {	
	    	return prepository.findById(playid);
	    }   
}
