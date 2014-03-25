package Business;

import Domain.IFactory;
import Domain.IRepository;

public class FactoryModel implements IFactory{

	@Override
	public IRepository GetRepository() {
		return new Repository();
	}

}
