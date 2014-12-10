package br.com.padtec.common.dto;

public enum DtoStatus {
	SATISFIED("Satisfied"), NOT_SATISFIED("Not Satified"), POSSIBLE_REFINEMENTS("Possible Refinements");

	private final String text;

    /**
     * @param text
     */
    private DtoStatus(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
