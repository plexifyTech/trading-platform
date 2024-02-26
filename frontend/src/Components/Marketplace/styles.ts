import styled from 'styled-components';

const FixedGrid = styled.div`
  @media only screen and (max-width: 750px) {
    display: flex;
    flex-direction: column;
  }
  @media only screen and (min-width: 960px) {
    max-width: 80%;
  }

  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr;
  text-align: center;
`;

const Wrapper = styled.div`
  display: flex;
  @media only screen and (max-width: 1470px) {
    flex-wrap: wrap;
    justify-content: center;
  }
`;

export { FixedGrid, Wrapper };
