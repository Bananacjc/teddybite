import React from 'react';
import { Box, Heading, Stat, StatLabel, StatNumber, StatHelpText, StatArrow, SimpleGrid, Card, CardBody } from '@chakra-ui/react';

const PaymentManagement = () => {
  return (
    <Box>
      <Heading size="lg" color="brown.900" mb={6}>Payment & Finance</Heading>

      <SimpleGrid columns={{ base: 1, md: 3 }} spacing={6} mb={8}>
        <Card borderRadius="xl" bg="white" boxShadow="sm">
          <CardBody>
            <Stat>
              <StatLabel>Total Revenue (Today)</StatLabel>
              <StatNumber>RM 1,245.00</StatNumber>
              <StatHelpText>
                <StatArrow type='increase' />
                23.36%
              </StatHelpText>
            </Stat>
          </CardBody>
        </Card>
        <Card borderRadius="xl" bg="white" boxShadow="sm">
          <CardBody>
            <Stat>
              <StatLabel>Transactions</StatLabel>
              <StatNumber>45</StatNumber>
              <StatHelpText>
                <StatArrow type='increase' />
                5%
              </StatHelpText>
            </Stat>
          </CardBody>
        </Card>
        <Card borderRadius="xl" bg="white" boxShadow="sm">
          <CardBody>
            <Stat>
              <StatLabel>Average Order Value</StatLabel>
              <StatNumber>RM 27.60</StatNumber>
              <StatHelpText>
                <StatArrow type='decrease' />
                2.1%
              </StatHelpText>
            </Stat>
          </CardBody>
        </Card>
      </SimpleGrid>

      <Heading size="md" mb={4}>Recent Transactions</Heading>
      {/* Add transaction table here similar to OrderManagement */}
      <Box p={4} bg="white" borderRadius="xl" boxShadow="sm">
          <Text color="gray.500" textAlign="center">No recent transactions found.</Text>
      </Box>
    </Box>
  );
};

export default PaymentManagement;
