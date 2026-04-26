package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.Souscription;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;

@Service
public class AttestationService {
	private final SouscriptionService souscriptionService;

	public AttestationService(SouscriptionService souscriptionService) {
		this.souscriptionService = souscriptionService;
	}

	@Transactional(readOnly = true)
	public byte[] generer(Long souscriptionId) {
		Souscription souscription = souscriptionService.rechercherEntite(souscriptionId);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		PdfWriter writer = new PdfWriter(outputStream);
		PdfDocument pdfDocument = new PdfDocument(writer);
		Document document = new Document(pdfDocument);

		document.add(new Paragraph("ATTESTATION D'ASSURANCE AUTOMOBILE"));
		document.add(new Paragraph("Numero attestation : " + souscription.getNumeroAttestation()));
		document.add(new Paragraph("Reference souscription : " + souscription.getSubscriptionReference()));
		document.add(new Paragraph("Produit : " + souscription.getSimulation().getProduitAssurance().getNom()));
		document.add(new Paragraph("Prime : " + souscription.getSimulation().getPrice() + " F CFA"));
		document.add(new Paragraph("Assure : " + souscription.getAssure().getPrenom() + " " + souscription.getAssure().getNom()));
		document.add(new Paragraph("Telephone : " + souscription.getAssure().getTelephone()));
		document.add(new Paragraph("Vehicule : " + souscription.getVehicule().getNumeroImmatriculation()));
		document.add(new Paragraph("Categorie : " + souscription.getVehicule().getCategorieVehicule().getCode()));
		document.close();

		return outputStream.toByteArray();
	}
}
