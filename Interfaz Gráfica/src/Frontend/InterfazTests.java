package Frontend;

import static org.junit.Assert.*;

import org.junit.Test;

import Frontend.InterfazUsuario;

public class InterfazTests{


	@Test
	public void testEliminarPestana() {
		InterfazUsuario i = new InterfazUsuario();
		i.nuevaPestana(1);
		InterfazUsuario.eliminarPestana(InterfazUsuario.onumero-1);
		assertEquals("test eliminar pestanaID", null, InterfazUsuario.pestanas[1]);
		
	}

	@Test
	public void testNuevaPestanaID() {
		//Hay que restarle 2 ya que los eventos crean 2 extras debido a los test
		//Si se hace un test unitario con impresiones se comprueba que está correcto.
		assertEquals("test agregar 1 pestanaID",InterfazUsuario.onumero-2, InterfazUsuario.pestanas[0].iD);
		InterfazUsuario i = new InterfazUsuario();
		i.nuevaPestana(1);
		assertEquals("test agregar 2 pestanaID",InterfazUsuario.onumero-2, i.pestanas[1].iD);
		i.nuevaPestana(1);
		assertEquals("test agregar 3 pestanaID", InterfazUsuario.onumero-2, i.pestanas[2].iD);
	}
	

}
