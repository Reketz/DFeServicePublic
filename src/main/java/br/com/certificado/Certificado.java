/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.certificado;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author SYSTECH
 */
public class Certificado {

    private String nome;
    private LocalDate vencimento;
    private LocalDateTime dataHoraVencimento;
    private Long diasRestantes;
    private String arquivo;
    private byte[] arquivoBytes;
    private String senha;
    private String cnpjCpf;
    private int tipoCertificado;
    private String dllA3;
    private String marcaA3;
    private String serialToken;
    private boolean valido;
    private boolean ativarProperties;
    private String sslProtocol;

    public Certificado() {
        this.ativarProperties = false;
        this.sslProtocol = "TLSv1.2";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public LocalDateTime getDataHoraVencimento() {
        return dataHoraVencimento;
    }

    public void setDataHoraVencimento(LocalDateTime dataHoraVencimento) {
        this.dataHoraVencimento = dataHoraVencimento;
    }

    public Long getDiasRestantes() {
        return diasRestantes;
    }

    public void setDiasRestantes(Long diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    public String getArquivo() {
        return arquivo;
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }

    public byte[] getArquivoBytes() {
        return arquivoBytes;
    }

    public void setArquivoBytes(byte[] arquivoBytes) {
        this.arquivoBytes = arquivoBytes;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public int getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(int tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public String getDllA3() {
        return dllA3;
    }

    public void setDllA3(String dllA3) {
        this.dllA3 = dllA3;
    }

    public String getMarcaA3() {
        return marcaA3;
    }

    public void setMarcaA3(String marcaA3) {
        this.marcaA3 = marcaA3;
    }

    public String getSerialToken() {
        return serialToken;
    }

    public void setSerialToken(String serialToken) {
        this.serialToken = serialToken;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public boolean isAtivarProperties() {
        return ativarProperties;
    }

    public void setAtivarProperties(boolean ativarProperties) {
        this.ativarProperties = ativarProperties;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public void setSslProtocol(String sslProtocol) {
        this.sslProtocol = sslProtocol;
    }
    
    
    
    
}
