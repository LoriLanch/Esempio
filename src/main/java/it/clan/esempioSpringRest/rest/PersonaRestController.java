package it.clan.esempioSpringRest.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.clan.esempioSpringRest.model.Persona;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/rest/persons",produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonaRestController {
	private List<Persona> persone;

	{
		persone = new ArrayList<Persona>();
		persone.add(new Persona(1,"Fulvio","Vola"));
		persone.add(new Persona(2,"Rino","Rano"));
		persone.add(new Persona(3,"Lorenzo","Lancioni"));
	}

	@GetMapping("/getAll")
	public List<Persona> getAll(){
		return persone;
	}

	@GetMapping("/getAll2")
	public ResponseEntity<List<Persona>> getAll2(){
		try {
			HttpStatus status = persone != null && persone.size()>0 ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<List<Persona>>(persone,status);
		} catch (Exception e) {
			return new ResponseEntity<List<Persona>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<Persona> getById(@PathVariable("id") Integer id) {
		try {
			Persona p = findById(id);
			HttpStatus status = p!=null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<Persona>(p, status);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Persona>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/getById/{id}/{nome}")
	public ResponseEntity<Persona> getByIdNome(@PathVariable("id") Integer id,@PathVariable("nome") String nome){
		try {
			Persona p = findByIdNome(id, nome);
			HttpStatus status = p!=null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<Persona>(p, status);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Persona>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(path = "/registra", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Persona registra(@RequestBody Persona p,@RequestHeader HttpHeaders httpHeaders) {
		String value = httpHeaders.getFirst("pippo");
		System.out.println(value);
		persone.add(p);
		return p;
	}

	@PutMapping(path = "/aggiornamentoCompleto",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Persona> aggiornamentoCompleto(@RequestBody Persona p) {
		if(p.getId() == null) {
			return new ResponseEntity<Persona>(HttpStatus.BAD_REQUEST);
		}
		Persona personaDB = findById(p.getId());
		if(personaDB==null) {
			return new ResponseEntity<Persona>(HttpStatus.NOT_FOUND);
		}
		personaDB.setNome(p.getNome());
		personaDB.setCognome(p.getCognome());
		return new ResponseEntity<Persona>(personaDB, HttpStatus.OK);
	}

	@PatchMapping(path = "/aggiornamentoParziale",consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Persona> aggiornamentoParziale(@RequestBody Persona p) {
		if(p.getId() == null) {
			return new ResponseEntity<Persona>(HttpStatus.BAD_REQUEST);
		}
		Persona personaDB = findById(p.getId());
		if(personaDB==null) {
			return new ResponseEntity<Persona>(HttpStatus.NOT_FOUND);
		}
		if(p.getNome()!=null && !p.getNome().isEmpty()) {
			personaDB.setNome(p.getNome());
		}
		if(p.getCognome()!=null && !p.getCognome().isEmpty()) {
			personaDB.setCognome(p.getCognome());
		}
		return new ResponseEntity<Persona>(personaDB, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancella(@PathVariable("id") Integer id) {
		Persona personaDB = findById(id);
		persone.remove(personaDB);
	}
	
	@DeleteMapping("/delete2/{id}")
	public ResponseEntity<Void> cancella2(@PathVariable("id") Integer id) {
		Persona personaDB = findById(id);
		if(personaDB==null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		persone.remove(personaDB);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping("/getByIdWithHeader/{id}")
	public ResponseEntity<Persona> getByIdWithHeader(@PathVariable("id") Integer id,
			@RequestHeader HttpHeaders headers) {
		try {
			String headerValue = headers.getFirst("pippo");
			System.out.println(headerValue);
			Persona persona = findById(id);
			HttpStatus status = persona != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
			return new ResponseEntity<Persona>(persona, status);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Persona>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Persona findById (Integer id) {
		for(Persona p : persone) {
			if(p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}
	
	private Persona findByIdNome (Integer id,String nome) {
		for(Persona p : persone) {
			if(p.getId().equals(id) && p.getNome().equalsIgnoreCase(nome)) {
				return p;
			}
		}
		return null;
	}
}
