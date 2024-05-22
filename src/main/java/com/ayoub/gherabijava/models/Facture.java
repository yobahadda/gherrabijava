package com.ayoub.gherabijava.models;
import java.sql.Date;
public class Facture {
    private int factureID;
    private int commandeID;
    private float montantTotal;
    private float poidsTotal;
    private Date dateFacture;
    public Facture(int commandeID, float montantTotal, float poidsTotal, Date dateFacture) {
        this.commandeID = commandeID;
        this.montantTotal = montantTotal;
        this.poidsTotal = poidsTotal; // Initialize the new attribute
        this.dateFacture = dateFacture;
    }
    public float getPoidsTotal() {
        return poidsTotal;
    }
    public void setPoidsTotal(float poidsTotal) {
        this.poidsTotal = poidsTotal;
    }
    public Facture(int commandeID, float montantTotal, Date dateFacture) {
        this.commandeID = commandeID;
        this.montantTotal = montantTotal;
        this.dateFacture = dateFacture;
    }

    public int getFactureID() {
        return factureID;
    }
    public void setFactureID(int factureID) {
        this.factureID = factureID;
    }
    public int getCommandeID() {
        return commandeID;
    }
    public void setCommandeID(int commandeID) {
        this.commandeID = commandeID;
    }
    public float getMontantTotal() {
        return montantTotal;
    }
    public void setMontantTotal(float montantTotal) {
        this.montantTotal = montantTotal;
    }
    public Date getDateFacture() {
        return dateFacture;
    }
    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }
    @Override
    public String toString() {
        return "Facture{" +
                "factureID=" + factureID +
                ", commandeID=" + commandeID +
                ", montantTotal=" + montantTotal +
                ", dateFacture=" + dateFacture +
                '}';
    }
}
