package com.eburtis.assurance.service;

import com.eburtis.assurance.domain.Assure;
import com.eburtis.assurance.domain.Simulation;
import com.eburtis.assurance.domain.Souscription;
import com.eburtis.assurance.domain.Vehicule;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class AttestationService {
	private static final DeviceRgb VIOLET_ACTION_ELLES = new DeviceRgb(80, 25, 132);
	private static final DeviceRgb ROSE_ACTION_ELLES = new DeviceRgb(210, 55, 145);
	private static final DeviceRgb FOND_CLAIR = new DeviceRgb(249, 241, 252);
	private static final DeviceRgb GRIS_TITRE = new DeviceRgb(45, 45, 45);
	private static final Locale LOCALE_FR = Locale.FRANCE;
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter DATE_HEURE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private final SouscriptionService souscriptionService;
	private final ResourceLoader resourceLoader;
	private final String logoPath;

	public AttestationService(SouscriptionService souscriptionService,
			ResourceLoader resourceLoader,
			@Value("${attestation.logo.path:classpath:static/images/logo-action-elles.png}") String logoPath) {
		this.souscriptionService = souscriptionService;
		this.resourceLoader = resourceLoader;
		this.logoPath = logoPath;
	}

	@Transactional(readOnly = true)
	public byte[] generer(Long souscriptionId) {
		Souscription souscription = souscriptionService.rechercherEntite(souscriptionId);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(outputStream);
		PdfDocument pdfDocument = new PdfDocument(writer);
		Document document = new Document(pdfDocument, PageSize.A4);
		document.setMargins(30, 34, 28, 34);

		ajouterEntete(document);
		ajouterBlocReference(document, souscription);
		ajouterInformations(document, souscription);
		ajouterQrCode(document, souscription);
		ajouterPiedDePage(document);

		document.close();
		return outputStream.toByteArray();
	}

	private void ajouterEntete(Document document) {
		Table entete = new Table(UnitValue.createPercentArray(new float[]{1.1f, 2.2f}));
		entete.setWidth(UnitValue.createPercentValue(100));
		entete.setMarginBottom(18);

		Cell logoCell = celluleSansBordure();
		Image logo = chargerLogo();
		if (logo != null) {
			logo.setMaxWidth(110);
			logo.setMaxHeight(60);
			logoCell.add(logo);
		} else {
			logoCell.add(new Paragraph("Action'Elles")
					.setFontSize(18)
					.setBold()
					.setFontColor(VIOLET_ACTION_ELLES));
		}

		Cell titreCell = celluleSansBordure()
				.setTextAlignment(TextAlignment.RIGHT)
				.setVerticalAlignment(VerticalAlignment.MIDDLE);
		titreCell.add(new Paragraph("ATTESTATION D'ASSURANCE AUTOMOBILE")
				.setFontSize(18)
				.setBold()
				.setFontColor(GRIS_TITRE));
		titreCell.add(new Paragraph("Document officiel de souscription")
				.setFontSize(10)
				.setFontColor(ColorConstants.GRAY));

		entete.addCell(logoCell);
		entete.addCell(titreCell);
		document.add(entete);
	}

	private void ajouterBlocReference(Document document, Souscription souscription) {
		Table table = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}));
		table.setWidth(UnitValue.createPercentValue(100));
		table.setMarginBottom(16);

		table.addCell(carteReference("N° attestation", souscription.getNumeroAttestation()));
		table.addCell(carteReference("Souscription", souscription.getSubscriptionReference()));
		table.addCell(carteReference("Statut", valeur(souscription.getStatut())));

		document.add(table);
	}

	private void ajouterInformations(Document document, Souscription souscription) {
		Simulation simulation = souscription.getSimulation();
		Assure assure = souscription.getAssure();
		Vehicule vehicule = souscription.getVehicule();

		Table colonnes = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
		colonnes.setWidth(UnitValue.createPercentValue(100));
		colonnes.setMarginBottom(14);

		colonnes.addCell(section("Assuré",
				ligne("Nom complet", nomComplet(assure.getPrenom(), assure.getNom())),
				ligne("Téléphone", assure.getTelephone()),
				ligne("Pièce d'identité", assure.getNumeroCarteIdentite()),
				ligne("Adresse", adresseAssure(assure))));

		colonnes.addCell(section("Véhicule",
				ligne("Immatriculation", vehicule.getNumeroImmatriculation()),
				ligne("Catégorie", vehicule.getCategorieVehicule().getCode()),
				ligne("Couleur", vehicule.getCouleur()),
				ligne("Mise en circulation", formatDate(vehicule.getDatePremiereMiseEnCirculation()))));

		document.add(colonnes);

		Table garantie = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
		garantie.setWidth(UnitValue.createPercentValue(100));
		garantie.setMarginBottom(16);
		garantie.addCell(section("Contrat",
				ligne("Produit", simulation.getProduitAssurance().getNom()),
				ligne("Référence devis", simulation.getQuoteReference()),
				ligne("Date de souscription", souscription.getDateSouscription().format(DATE_HEURE_FORMAT)),
				ligne("Validité du devis", formatDate(simulation.getEndDate()))));
		garantie.addCell(section("Prime",
				ligne("Montant", formatMontant(simulation.getPrice())),
				ligne("Puissance fiscale", valeur(vehicule.getPuissanceFiscale()) + " CV"),
				ligne("Valeur neuve", formatMontant(vehicule.getValeurNeuve())),
				ligne("Valeur vénale", formatMontant(vehicule.getValeurVenale()))));

		document.add(garantie);
	}

	private void ajouterQrCode(Document document, Souscription souscription) {
		String contenuQr = "Action'Elles - Attestation\n"
				+ "Numéro: " + souscription.getNumeroAttestation() + "\n"
				+ "Souscription: " + souscription.getSubscriptionReference() + "\n"
				+ "Statut: " + souscription.getStatut() + "\n"
				+ "Produit: " + souscription.getSimulation().getProduitAssurance().getNom() + "\n"
				+ "Assuré: " + nomComplet(souscription.getAssure().getPrenom(), souscription.getAssure().getNom()) + "\n"
				+ "Immatriculation: " + souscription.getVehicule().getNumeroImmatriculation();

		BarcodeQRCode qrCode = new BarcodeQRCode(contenuQr);
		PdfFormXObject qrObject = qrCode.createFormXObject(ColorConstants.BLACK, document.getPdfDocument());
		Image qrImage = new Image(qrObject).setWidth(92).setHeight(92);

		Table table = new Table(UnitValue.createPercentArray(new float[]{1.7f, 1}));
		table.setWidth(UnitValue.createPercentValue(100));
		table.setBorder(new SolidBorder(VIOLET_ACTION_ELLES, 1));
		table.setMarginBottom(14);

		Cell texte = new Cell()
				.setBorder(Border.NO_BORDER)
				.setPadding(12)
				.setBackgroundColor(FOND_CLAIR);
		texte.add(new Paragraph("Vérification de l'attestation")
				.setBold()
				.setFontSize(12)
				.setFontColor(VIOLET_ACTION_ELLES));
		texte.add(new Paragraph("Scannez le QR code pour lire les informations principales de cette attestation.")
				.setFontSize(9)
				.setFontColor(GRIS_TITRE));

		Cell qr = new Cell()
				.setBorder(Border.NO_BORDER)
				.setPadding(10)
				.setTextAlignment(TextAlignment.CENTER)
				.setVerticalAlignment(VerticalAlignment.MIDDLE);
		qr.add(qrImage);

		table.addCell(texte);
		table.addCell(qr);
		document.add(table);
	}

	private void ajouterPiedDePage(Document document) {
		document.add(new Paragraph("Cette attestation est générée automatiquement par Action'Elles. Elle doit être vérifiée avant toute prise en charge.")
				.setFontSize(8)
				.setTextAlignment(TextAlignment.CENTER)
				.setFontColor(ColorConstants.GRAY));
	}

	private Cell carteReference(String libelle, Object valeur) {
		Cell cell = new Cell()
				.setPadding(10)
				.setBorder(new SolidBorder(FOND_CLAIR, 1))
				.setBackgroundColor(FOND_CLAIR);
		cell.add(new Paragraph(libelle.toUpperCase(LOCALE_FR))
				.setFontSize(8)
				.setFontColor(ROSE_ACTION_ELLES)
				.setBold());
		cell.add(new Paragraph(valeur(valeur))
				.setFontSize(12)
				.setFontColor(GRIS_TITRE)
				.setBold());
		return cell;
	}

	private Cell section(String titre, Table... lignes) {
		Cell cell = new Cell()
				.setPadding(12)
				.setBorder(new SolidBorder(new DeviceRgb(230, 230, 230), 1));
		cell.add(new Paragraph(titre)
				.setBold()
				.setFontSize(12)
				.setFontColor(VIOLET_ACTION_ELLES)
				.setMarginBottom(8));
		for (Table ligne : lignes) {
			cell.add(ligne);
		}
		return cell;
	}

	private Table ligne(String libelle, Object valeur) {
		Table table = new Table(UnitValue.createPercentArray(new float[]{0.9f, 1.4f}));
		table.setWidth(UnitValue.createPercentValue(100));
		table.addCell(celluleLibelle(libelle));
		table.addCell(celluleValeur(valeur(valeur)));
		return table;
	}

	private Cell celluleLibelle(String texte) {
		return celluleSansBordure()
				.add(new Paragraph(texte)
						.setFontSize(8)
						.setFontColor(ColorConstants.GRAY));
	}

	private Cell celluleValeur(String texte) {
		return celluleSansBordure()
				.add(new Paragraph(texte)
						.setFontSize(9)
						.setFontColor(GRIS_TITRE)
						.setBold());
	}

	private Cell celluleSansBordure() {
		return new Cell().setBorder(Border.NO_BORDER);
	}

	private Image chargerLogo() {
		try {
			Resource resource = resourceLoader.getResource(logoPath);
			if (!resource.exists()) {
				return null;
			}
			ImageData data = ImageDataFactory.create(resource.getContentAsByteArray());
			return new Image(data);
		} catch (IOException exception) {
			return null;
		}
	}

	private String adresseAssure(Assure assure) {
		String ville = valeur(assure.getVille());
		String adresse = valeur(assure.getAdresse());
		if ("-".equals(ville)) {
			return adresse;
		}
		if ("-".equals(adresse)) {
			return ville;
		}
		return adresse + ", " + ville;
	}

	private String nomComplet(String prenom, String nom) {
		return (valeur(prenom) + " " + valeur(nom)).trim();
	}

	private String formatMontant(BigDecimal montant) {
		if (montant == null) {
			return "-";
		}
		NumberFormat format = NumberFormat.getNumberInstance(LOCALE_FR);
		format.setMaximumFractionDigits(0);
		return format.format(montant) + " F CFA";
	}

	private String formatDate(java.time.LocalDate date) {
		return date == null ? "-" : date.format(DATE_FORMAT);
	}

	private String valeur(Object valeur) {
		if (valeur == null) {
			return "-";
		}
		String texte = String.valueOf(valeur).trim();
		return texte.isEmpty() ? "-" : texte;
	}
}
